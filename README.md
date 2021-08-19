# UMGC Computer Science ProjectS
### A small collection

## Project Summary By Language
# C
### Banker Algorithm
#### Class: Operating Systems
#### Summary:
    This project was created to receive an input file to create a resource vector, claim matrix, and allocation matrix to create a needs matrix. It finds an available resources. It uses the bankers algorithm to discover safe paths.

    To be completely honest, I don't remember the purpose of this software. I was just excited to program in C. I had previous experience with C++ and wanted to sharpen my tools. As there was no native stack data structure available, I had to create functions that emulated a stack.

### Directory Actions
#### Class: Operating Systems
#### Summary:
This project was also created in C. I made it a point to program only in C for my Operating Systems class. This is because many linux kernels and micro-controllers use C. I had the choice to use Java, but I leaned heavily into C. It was a great opportunity for me to become more familiar with C concepts such as pointers.

This project performs various directory based actions. This includes listing directory contents in a single directory or recursively, delete files and encrypt files.

# Java
### Personally Identifiable Information Protection (PIIP)
#### Class: Current Trends and Projects In Computer Science
#### Summary:
Initially, I was supposed to be the team project manager for this project. But, I quickly assumed the lead software development role. This was due to many factors. The software intent was to encrypt and decrypt files in a way that required multiple forms of authentication. The idea was inspired by the 16 & 17 CFR for financial advisers. They are required to safeguard their clients sensitive information.

This program aims to give them a means to encrypt their data using asymmetric & symmetric enccryption. In addition, to support two-factor authentication, they are required to export their RSA public key to a USB. This supports the factor of "something they have". They would be required to register a password for the "something they know" factor.

Below is a general feature list of the software:

- Generation of RSA 2048 keys
- Combination of AES (symmetric) and RSA (asymmetric) encryption
- SQLite instance to store all events for logging
---* Different events are logged and digitally signed to support non-repudiation
- OS Independent
- Portable
--* (Can be moved from workstation to workstation and still work given that the correct public key and password are supplied)

Though the software was not complete at the due date, I still received a grade of 96%. This is a project that I continue to work on despite it's many flaws currently. Future iterations will change the code structure to support a higher cohesion and lower coupling. 
    
Included in this summary is the Phase 3 (of 4 phases) presentation of the program. Below are the links to the youtube videos. They include a demonstration of the program on Windows, Linux and Mac environments with commentary.

[PHASE 3 TESTING - Part 1](https://youtu.be/UmQPMBZLM_M)
[PHASE 3 TESTING - Part 2](https://youtu.be/NH7DPovluq0)
[PHASE 3 TESTING - Part 3](https://youtu.be/ShGPdbcrNU8)
[PHASE 3 TESTING - Part 4](https://youtu.be/8Alq4wkwElA)
[PHASE 3 TESTING - Part 5](https://youtu.be/F9S5rNZCy7s)
[PHASE 3 TESTING - Part 6](https://youtu.be/CfYQ4FZfGRs)
    
The primary difference between Phase 3 and Phase 4 is the password capture gui and input sanitization. This was performed by the other software developer on the team. The functionality was not completely tested. 

In conclusion, I loved creating the components of this project because it allowed me to cross the security domain with the software engineering domain.

### Binary Tree Creation
#### Class: Data Structures & Analysis
#### Summary:
This project was my first introduction to Binary Trees. It was this project in which I became more comfortable with recursive functions. This was also my first introduction into the importance of BigO analysis. The dangerous thing about most of the functions in this application is that the BigO are exponential. At the time, I didn't realize the importance of keeping the height of the tree low. After my algorithms analysis class, I did understand though.

# Python
### Generic Flask App without Database Storage
#### Class: Building Secure Python Applications
#### Summary:
This was the final project for this class. It is a generic Flask app that utilizes a json flat file to store users and their passwords. This app is based on the MVC structure. The users utilize Python Objects to create the model to pass data. This project utilizes AJAX to create an asynchronous experience. In addition, I utilized pyenv to manage dependencies.

Overall, I had fun with this project. Prior to my university studies, I had experience building webapps & websites using Ruby on Rails and Express.js. Creating RESTful websites and utilizing a MVC framework was second nature
