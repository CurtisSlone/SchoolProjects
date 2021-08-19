/*H********************
* FILENAME : week6.c
*
* DECRIPTION : 
*   Program that allows users to perform various actions in the within a directory
*
* AUTHOR: Curtis Slone
* 
* DATE : 18 Feb 2021
*
*H*/
#include <stdio.h>
#include <dirent.h>
#include <stdbool.h>
#include <string.h>
#include <signal.h>
#include <sys/types.h>
#include <stdlib.h>
#include <unistd.h>

#define MAX 255

//GLOBAL VARIABLES
char path[MAX];
char file[MAX];
char outFile[MAX];
char *buff;
char *tmp;
char ch;
struct dirent *de;
DIR *dr;
FILE *fp, *fpo;
bool dirChosen = false;
char password[];
char *key;
char userInput[];
//QUEUE RELATED ATTRIBUTES
char *pathArray[MAX];
int front = 0;
int rear = -1;
int itemCount = 0;
//HOISTEAD FUNCTIONS
void getDir();
int firstLevelContents();
int allLevelContents();
char* peek();
bool isEmpty();
bool isFull();
int size();
void insert();
void removeData();
int deleteFile();
int hexDump();
int encryptFile();
int decryptFile();
int segFaultException(int);

// MAIN FUNCTION
int main()
{
    signal(SIGSEGV, segFaultException);
    while(1)
    {
        printf("Choose the action you would like to do by entering the corresponding number\n");
        printf("0 - Exit\n");
        printf("1 - Select Directory\n");
        printf("2 - List directory contents (first level)\n");
        printf("3 - List directory contents (all levels)\n");
        printf("4 - Delete File\n");
        printf("5 - Display File (hexadecimal view)\n");
        printf("6 - Encrypt file (XOR with password)\n");
        printf("7 - Decrypt file (XOR with password\n");
        printf("Selection option:\n");
        scanf("%s",userInput);
        if ( userInput[0] == '0')
        {
            exit(0);
        }
        else
        if ( userInput[0] == '1')
        {
            getDir();
        }
        else
        if ( userInput[0] == '2')
        {
            firstLevelContents();
        }
        else
        if ( userInput[0] == '3')
        {
            allLevelContents();
        }
        else
        if ( userInput[0] == '4')
        {
            deleteFile();
        }
        else
        if ( userInput[0] == '5')
        {
            hexDump();
        }
        else
        if ( userInput[0] == '6')
        {
            encryptFile();
        }
        else
        if ( userInput[0] == '7')
        {
            decryptFile();
        }
        else
        {
            printf("Invalid input. Please try again");
        }
    }
    return 0;
}

//PROGRAM FUNCTIONS

//SELECT DIRECTORY
void getDir()
{
    dirChosen = true;
    printf("Please type absolute path of the directory > ");
    scanf("%s", path);
    insert(path);
}

//List first level of directory
int firstLevelContents()
{   if (dirChosen == false)
    {
        printf("No directory chosen. Using current directory\n");
        strcpy(path,".");
        printf("path > %s\n",path);
    }
    dr = opendir(path);
    if (dr == NULL)
    {
        printf("\nCould not open directory");
    }
    printf("dir : %s \n", path);
    while((de = readdir(dr)) != NULL)
    {
        if(de->d_type == DT_DIR)
            printf("%s <--- Directory \n", de->d_name);
        else
            printf("%s \n", de->d_name);
    }
    closedir(dr);
    return 0;
}

//List all levels of directory
int allLevelContents()
{
    if (dirChosen == false)
    {
        printf("No directory chosen. Using current directory\n");
        strcpy(path,".");
    }
    tmp = (char*)malloc(sizeof(peek()));
    strcpy(tmp,peek());
    printf("---------------------------");
    printf("\nUsing directory %s \n",tmp);
    dr = opendir(tmp);
    if (dr == NULL)
    {
        printf("\nCould not open directory");
    }
    while((de = readdir(dr)) != NULL)
    {
        if(de->d_type == DT_DIR && strcmp(de->d_name, ".") != 0 && strcmp(de->d_name, "..") != 0 )
        {
            tmp = (char *)realloc(tmp,sizeof(de->d_name));
            strcat(tmp,"/");
            strcat(tmp,de->d_name);
            insert(tmp);
            tmp = (char*)malloc(sizeof(peek()));
            strcpy(tmp,peek());
            sleep(1);
            printf("%s <-- Directory \n", de->d_name);

        }
        else
        {
        printf("%s \n", de->d_name);
        }
    }
    removeData();
    closedir(dr);
    if(!isEmpty())
    {
        allLevelContents();
    }   
    return 0;
}

//DELETE FILE
int deleteFile()
{
    if (dirChosen == false)
    {
        printf("No directory chosen. Using current directory\n");
        strcpy(path,".");
        printf("path > %s\n",path);
    }
    // bool found = false;
    printf("Please choose a file to delete > ");
    scanf("%s", file);
    tmp = (char*)malloc(sizeof(path)+ sizeof(file)+1);
    strcpy(tmp,path);
    strcat(tmp,"/");
    strcat(tmp,file);
    if(remove(tmp) == 0)
        printf("%s deleted sucessfully\n",tmp);
    else
        printf("Unable to delete file. Please re-enter correct file name");

    return 0;
}
// HEX DUMP

int hexDump()
{
    if (dirChosen == false)
    {
        printf("No directory chosen. Using current directory\n");
        strcpy(path,".");
        printf("path > %s\n",path);
    }
    printf("Please choose select file for hex view > ");
    scanf("%s", file);
    tmp = (char*)malloc(sizeof(path)+ sizeof(file)+1);
    strcpy(tmp,path);
    strcat(tmp,"/");
    strcat(tmp,file);
    fp = fopen(file,"r");
    if(fp == NULL)
    {
        printf("Could not open file\n");
        return 0;
    }

    printf("Contents of %s file: \n",file);
    int i;
    while( ( ch = fgetc(fp) ) != EOF )
    {
        printf("%02X ", ch);
        if(!(++i % 16))
        {
            putc('\n', stdout);
        }
    }
    fclose(fp);
    putc('\n',stdout);
    return 0;
}

//Encrypt using XOR password
int encryptFile()
{
    int i = 0;
    printf("Please type a password (maximum 256 bytes) to encrypt your file with. Do not include spaces. > ");
    scanf("%s", password);
    
    for(i = 0; i < strlen(password);i++)
    {
        key += (int)password[i];
    }
    printf("key > %d \n",key);

    if (dirChosen == false)
    {
        printf("No directory chosen. Using current directory\n");
        strcpy(path,".");
    }
    printf("Please choose select file for encryption > ");
    scanf("%s", file);
    printf("Please choose select name of encrypted file to be output > ");
    scanf("%s", outFile);

    tmp = (char*)malloc(sizeof(path)+ sizeof(file)+1);
    strcpy(tmp,path);
    strcat(tmp,"/");
    strcat(tmp,file);
    fp = fopen(file,"r");

    tmp = (char*)malloc(sizeof(path)+ sizeof(file)+1);
    strcpy(tmp,path);
    strcat(tmp,"/");
    strcat(tmp,outFile);
    fpo = fopen(outFile, "w");

    if(fp == NULL)
    {
        printf("Could not open file\n");
        return 0;
    }

    if(fpo == NULL)
    {
        printf("No outfile selected. Writing over selected infile.\n");
        fclose(fpo);
        fpo = fopen(file,"w");
        return 0;
    }
    while( ( ch = fgetc(fp) ) != EOF )
    {
        fprintf(fpo, "%c", (ch ^ (int)key) );  
    }
    fclose(fpo);
    fclose(fp);
    
   key = NULL;
    return 0;
}

//Decrypt using XOR password
int decryptFile()
{
    int i = 0;
    printf("Please type the password you used to encrypt the file > ");
    scanf("%s", password);
    for(i = 0; i < strlen(password);i++)
    {
        key += (int)password[i];
    }
     printf("key > %d \n",key);
    if (dirChosen == false)
    {
        printf("No directory chosen. Using current directory\n");
        strcpy(path,".");
    }
    printf("Please choose select file for decryption > ");
    scanf("%s", file);
    printf("Please choose select name of decrypted file to be output > ");
    scanf("%s", outFile);

    tmp = (char*)malloc(sizeof(path)+ sizeof(file)+1);
    strcpy(tmp,path);
    strcat(tmp,"/");
    strcat(tmp,file);
    fp = fopen(file,"r");

    tmp = (char*)malloc(sizeof(path)+ sizeof(file)+1);
    strcpy(tmp,path);
    strcat(tmp,"/");
    strcat(tmp,outFile);
    fpo = fopen(outFile, "w");

    if(fp == NULL)
    {
        printf("Could not open file\n");
        return 0;
    }

    if(fpo == NULL)
    {
        printf("No outfile selected. Writing over selected infile.\n");
        fclose(fpo);
        fpo = fopen(file,"w");
        return 0;
    }
    while( ( ch = fgetc(fp) ) != EOF )
    {
        fprintf(fpo, "%c", (ch ^ (int)key) );  
    }
    fclose(fpo);
    fclose(fp);
    
    key = NULL;
    return 0;
}

// QUEUE IMPLEMENTATION FUNCTIONS
char* peek()
{
    return pathArray[front];
}

bool isEmpty()
{

    return itemCount == 0;
}

bool isFull()
{
    return itemCount == MAX;
}

int size()
{
    return itemCount;
}

void insert(char *data){
    if(!isFull())
    {
        if(rear == MAX - 1)
        {
            rear = -1;
        }
        pathArray[++rear] = data;
        itemCount++;
    }
}

void removeData()
{       front++;
    if(front == MAX)
    {
        front = 0;
    }
    itemCount--;
}

//SEGMENTATION FAULT HANDLING FOR INCORRECT DIR
int segFaultException(int sig_num)
{
    printf("\n Signal %d received. Directory not found",sig_num);
    return 0;
}