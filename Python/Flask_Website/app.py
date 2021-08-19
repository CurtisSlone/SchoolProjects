"""
File: app.py
Author: Curtis Slone
Date: 12 Feb 2021
Purpose: Basic Flask Website With Password Entry and Input Validation
"""
import random
from datetime import datetime
import json
from flask import Flask, render_template, request, jsonify, redirect, url_for
from passlib.hash import sha256_crypt
from flask_login import LoginManager, login_required, \
                        current_user, login_user, logout_user, UserMixin
app = Flask(__name__)
app.config['SECRET_KEY'] = 'thisismysecretkeydonotstealit'
login_manager = LoginManager()
login_manager.init_app(app)
#################
### ROUTES ###
#################
@app.route('/')
def home():
    """Return hompage"""
    return render_template('home.html')
@app.route('/sign-up', methods=['POST'])
def sign_up():
    """ Process Form"""
    name = request.form['name']
    password = request.form['pass']
    email = request.form['email']
    ### Create Model Object, Hash Password Add To Flat File ##
    if name and password and email:
        new_member = User()
        new_member.id = random.randrange(400)
        new_member.name = name
        new_member.password = do_hash_password(password)
        new_member.email = email
        send_model_object_to_flat_file(new_member)
        return json.dumps({'success' : 'Go ahead and sign-in to access the member\'s area!'})
    return json.dumps({'error' : 'Missing data. Check your information!'})
@app.route('/member')
@login_required
def member():
    """ member only area """
    return render_template('members.html',name=current_user.name)
@app.route('/password-change')
@login_required
def password_change():
    """ Change password """
    return render_template('password.html',name=current_user.name)
@app.route('/email-check')
def email_check():
    """ check members file for currently used email """
    tmp = return_members_list_from_flat_file()
    tmp_dict = {}
    i = 0
    for mem in tmp:
        email = mem.get('EMAIL')
        tmp_dict[i] = email
        i += 1
    return jsonify(tmp_dict)
@app.route('/password-check', methods=['POST'])
@login_required
def password_check():
    """ compare common paswords file and new-password input"""
    tmp_list = open("CommonPassword.txt").readlines()
    pass_list = [x[:-1] for x in tmp_list]
    old_password = request.form['old_password']
    new_password = request.form['new_password']
    verify_new_pass = request.form['verify_new_pass']
    members_list = return_members_list_from_flat_file()
    hashed_password = " "
    for memb in members_list:
        if memb['EMAIL'] == current_user.email:
            if check_hash_password(old_password, memb['PASSHASH']):
                hashed_password = memb['PASSHASH']
                break
    try:
        check_hash_password(old_password,hashed_password)
    except ValueError:
        error = "Incorrect Old Password"
        return jsonify({'error' : error})
    if new_password != verify_new_pass:
        error = "New Passwords Don't Match"
        return jsonify({'error' : error})
    for i in pass_list:
        if i == new_password:
            error = "New Password Matched Compromised Password"
            return jsonify({'error' : error})
    for memb in members_list:
        if memb['EMAIL'] == current_user.email:
            memb['PASSHASH'] = do_hash_password(new_password)
    jsond = {
        "members" : members_list
    }
    with open('members.json','w') as write_file:
        json.dump(jsond,write_file,indent=4)
    return jsonify({'success' : 'You successfully changed your password'})
@app.route('/login', methods=['POST'])
def login():
    """login form """
    ip_address = request.remote_addr
    now = datetime.now()
    date_time = now.strftime("%d/%m/%Y, %H:%M:%S")
    email = request.form['email']
    password = request.form['pass']
    user = False
    hash_match = False
    temp = return_members_list_from_flat_file()
    for memb in temp:
        if memb['EMAIL'] == email:
            user = True
            if check_hash_password(password, memb['PASSHASH']):
                hash_match = True
                break
    if user and hash_match:
        member_to_login = get_member_object(temp,email)
        login_user(member_to_login)
        return jsonify({'success' : 'Awesome! Redirecting you now.'})
    if user and not hash_match:
        file_object = open('log.txt', 'a')
        file_object.write('\\ FAILED LOGIN : ' + date_time + ' IP: ' + ip_address)
        return jsonify({'error' : 'Invalid Password'})
    if not user and not hash_match:
        file_object = open('log.txt', 'a')
        file_object.write('\n\\\\ FAILED LOGIN : ' + date_time + ' IP: ' + ip_address + '\n')
        return jsonify({'error' : 'Couldn\'t log you in. Check your information or sign-up!'})
@app.route('/logout')
@login_required
def logout():
    """ Log User Out """
    logout_user()
    return redirect(url_for('home'))
#################
## USER LOADER ##
#################
@login_manager.user_loader
def load_user(user_id):
    """ Load User based on member id """
    temp = return_members_list_from_flat_file()
    for memb in temp:
        if memb['ID'] == int(user_id):
            loaded_member = get_member_object(temp,memb['EMAIL'])
            return loaded_member
    return jsonify("User not loaded")
#################
## APP METHODS ##
#################
def do_hash_password(pass_string):
    """ hash password """
    print(f'{pass_string}')
    return sha256_crypt.hash(pass_string)
def check_hash_password(pass_string, hash_string):
    """ unhash password """
    return sha256_crypt.verify(pass_string,hash_string)
def send_model_object_to_flat_file(new_user):
    """ Convert object to JSON format and dump onto file"""
    raw_data = {
        'ID' : new_user.id,
        'NAME' : new_user.name,
        'PASSHASH' : new_user.password,
        'EMAIL' : new_user.email
    }
    temp = return_members_list_from_flat_file()
    temp.append(raw_data)
    jsond = {
        "members" : temp
    }
    with open('members.json','w') as write_file:
        json.dump(jsond,write_file,indent=4)
def return_members_list_from_flat_file():
    """ return mambers list """
    with open("members.json","r") as read_file:
        data = json.load(read_file)
    return data['members']
def search_member(member_email):
    """ search member based on member_email """
    members_list = return_members_list_from_flat_file()
    for memb in members_list:
        if member_email == memb['EMAIL']:
            return memb['EMAIL']
    return jsonify({'error' : 'Email not found'})
def get_member_object(memberlist,member_email):
    """ get user object from flat file base on email"""
    for memb in memberlist:
        if memb['EMAIL'] == member_email:
            user_login = User()
            user_login.id = memb['ID']
            user_login.name = memb['NAME']
            user_login.email = memb['EMAIL']
            return user_login
    return json.dumps({'Error' : 'Unable to find member information'})
#################
## MODELS ##
#################
class User(UserMixin):
    """ User Class """
    id = " "
    name = " "
    password = " "
    email = " "
    