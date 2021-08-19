/*H********************
* FILENAME : bankerAlgotrithm.c
*
* DECRIPTION : 
*   Uses input file to create resource vector, claim matrix, and allocation matrix
*   creates need matrix
*   finds available resources
*   uses banker algorithm to discover available safe paths
*
* AUTHOR: Curtis Slone
* 
* DATE : 06 Feb 2021
*
*H*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>


char filename[32] = {'\0'};
char buff[255];
char tmp;
char* token;
int numProc;
int numRes;
FILE *inputFile;
int i;
int j;
int resVect[10];
int claimMatrix[10][10];
int allocMatrix[10][10];
int needMatrix[10][10];
int availRes[10];
int work[10];
int step = 1;
int numChoices;
int currentChoices[10];
int savedStep = 0;
int usedChoices[10][10];
int MAXSIZE = 10;       
int stack[10];     
int top = -1;  
int usedArrayIndex = 0;





//Get File Data
void getFileData()
{
    printf("Please type filename > ");
    scanf("%s",filename);
    printf("\nFile name: %s \n",filename);
    inputFile = fopen(filename,"r");
    if(inputFile == NULL)
    {
        printf("Could not open file\n");
    }
    //get number of Process
    fgets(buff,255,(FILE*) inputFile);
    tmp = buff[0];
    numProc = atoi(&tmp);

    //get number of Resources
    fgets(buff,255,(FILE*) inputFile);
    tmp = buff[0];
    numRes = atoi(&tmp);

    //initialize matrices and vectors
    

    //get resource vector
    fgets(buff,255,(FILE*) inputFile);
    token = strtok(buff," ");
    for(i = 0;i < numRes;i++)
    {
        tmp = atoi(token);
        resVect[i] = tmp;
        token = strtok(NULL," ");
    }

    //get claim matrix
    for (i = 1; i <= numProc; i++)
    {
        fgets(buff,255,(FILE*) inputFile);
        token = strtok(buff," ");
        for(j = 0; j < numRes;j++)
        {
        tmp = atoi(token);
        claimMatrix[i][j] = tmp;
        token = strtok(NULL," ");
        }
    }

    //get allocation matrix
    for (i = 1; i <= numProc; i++)
    {
        fgets(buff,255,(FILE*) inputFile);
        token = strtok(buff," ");
        for(j = 0; j < numRes;j++)
        {
        tmp = atoi(token);
        allocMatrix[i][j] = tmp;
        token = strtok(NULL," ");
        }
    }
    fclose(inputFile);
}

//get need matrix
void getNeedMatrix()
{
    for(i = 1; i <= numProc;i++)
    {
        for(j = 0; j < numRes; j++)
        {
            //need = alloc - claim
            needMatrix[i][j] = claimMatrix[i][j] - allocMatrix[i][j];
        }
    }
}

//get available resources
void getAvailRes()
{
    //Available Resources = resoure Vector element - sum of elements in same index of allocation matrix

    int sum;
    for(i = 0; i < numRes; i++)
    {
        sum = 0;
        for(j = 1; j <= numProc; j++)
        {
            sum += allocMatrix[j][i];
        }
        availRes[i] = resVect[i] - sum;
    }
}


void createNewWork()
{
    //create work copy of avail resources
    for (i = 0; i < numRes;i++)
    {
        work[i] = availRes[i];
    }
}

//Safe Path finder using safety algorithm
int safetyCheck(int nodeIndex)
{
    int safe = 1;
    //compare node need to work
    //determine if process request is safe
    for (i = 0; i < numRes; i++)
    {
        if(needMatrix[nodeIndex][i] > work[i] )
        {
            safe = 0;
            break;
        } 
    }

   return safe;
}


// Stack functions to track choices
          

int isempty() {

   if(top == -1)
      return 1;
   else
      return 0;
}
   
int isfull() {

   if(top == MAXSIZE)
      return 1;
   else
      return 0;
}

int peek() {
   return stack[top];
}

int pop() {
   int data;
	
   if(!isempty()) {
      data = stack[top];
      top = top - 1;   
      return data;
   } else {
      printf("Could not retrieve data, Stack is empty.\n");
   }
}

int push(int data) {

   if(!isfull()) {
      top = top + 1;   
      stack[top] = data;
   } else {
      printf("Could not insert data, Stack is full.\n");
   }
}

int checkUsedArrayIndex(int step)
{

    for (i = 0; i < numProc;i++)
    {
        if(usedChoices[step][i] == 0)
        {
            return i;
        }
    }

}
int checkInCurrentPath(int index)
{
    int safe = 1;
for (int k = 0; k <= top; k++)
{
    if (stack[k] == index)
    {
        safe = 0;
        return safe;
    }
}
return safe;
}

int checkUsedArray(int index)
{
    int safe = 1;

for (int k = 0; k <= numProc; k++)
{
    if (usedChoices[step+1][k] == index)
    {
        safe = 0;
        return safe;
    }

}
return safe;
}

void newUsedChoicesArray()
{
    usedArrayIndex = 0;
    for(i = 0; i < numProc; i++)
    {
        usedChoices[step][i] = 0;
    }
}

void clearUsedChoicesArray()
{
    usedArrayIndex = 0;
    for(i = 0; i < numProc; i++)
    {
        usedChoices[step+2][i] = 0;
    }
}
 
void procChoices()
{
    numChoices = 0;
    for(j = 0; j <= numProc; j++)
    {   
        if((safetyCheck(j) == 1) && (checkUsedArray(j) == 1 ) && (checkInCurrentPath(j) == 1))
        {
            currentChoices[numChoices] = j;
            numChoices++;
        }
    }
}

void stepForward(int firstChoice)
{
    if( step <= (numProc))
    {
    push(firstChoice);
    for(i = 0; i < numRes; i++)
    {
        work[i] = work[i] + allocMatrix[firstChoice][i];
    }
    }
    else
    {
        printf("Can't step forward any more");
    }
}

void stepBack()
{
    
    if(isempty() == 0){
        
        for(i = 0; i < numRes; i++)
        {
            work[i] = work[i] - allocMatrix[peek()][i];
        }
        pop();
        step--;
    }
    else
    {
        printf("Failed Somewhere");
        exit(0);
    }
}

void clearChoiceArray()
{
    for ( i = 0; i < numProc; i++)
    {
        currentChoices[i] = 0;
    }
}

void printPath()
{
    printf("Safe Path: ");
    for(i = 0; i < numProc;i++)
    {
        printf("%d ", stack[i]);
    }
    printf("\n");
}

void gotoLastSavedStep()
{
    while(step != savedStep)
    {
        stepBack();
    }
    procChoices();
    usedChoices[step+1][checkUsedArrayIndex(step+1)] = currentChoices[0];
    clearUsedChoicesArray();
}

int main ()
{
    getFileData();
    getNeedMatrix();
    getAvailRes();
    createNewWork();
    newUsedChoicesArray();
    while (step <= (numProc + 1) || step < 0)
    {

        
        clearChoiceArray();
        procChoices();
        if (numChoices > 1)
        {
            stepForward(currentChoices[0]);
            step++;
            savedStep++;
        }
        else
        if (numChoices == 1 )
        {
            stepForward(currentChoices[0]);
            if (step == numProc)
            {
                printPath();
            }
            else
            {
                step++;
            }
            
        }
        else
        if (numChoices == 0)
        {
            if (step == numProc)
            {
                printPath();
                gotoLastSavedStep();
            }
            else
            if( isempty() == 1)
            {
                printf("\nSystem in deadlock. Exiting program");
                exit(0);
            }
            else
            if(step == savedStep)
            {
                savedStep--;
                gotoLastSavedStep();
            }
            
            
            
            
        }
       
    }
    
    
   
    
    return(0);
}

