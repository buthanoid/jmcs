#ifndef MCS_H
#define MCS_H
/*******************************************************************************
* JMMC project
*
* "@(#) $Id: mcs.h,v 1.13 2004-12-15 10:28:30 lafrasse Exp $"
*
* who       when		 what
* --------  -----------	 -------------------------------------------------------
* mella     07-May-2004  Preliminary version based on MCS from VLT/ESO
* gzins     11-May-2004  Add RCS Id and removed unused definitions
* lafrasse  01-Dec-2004  Added mcsUNKNOWN_ENV
* gzins     09-Dec-2004  Defined mcsLOGICAL as an enumerate
* lafrasse  14-Dec-2004  Increased environment name length constant
*                        mcsENVNAME_LEN from 7 to 15
*
*
*******************************************************************************/

/* The following piece of code alternates the linkage type to C for all 
functions declared within the braces, which is necessary to use the 
functions in C++-code.
*/

#ifdef __cplusplus
extern "C" {
#endif

/************************************************************************
 *                           MCS  Constants                             *
 ************************************************************************/
#define mcsPROCNAME_LEN        19   /* max. length of a process name    */
#define mcsMODULEID_LEN         7   /* max. length of a module name     */
                                    /* 6 characters + 1 byte alignement */
#define mcsENVNAME_LEN         15   /* max. length of an environnement  */
                                    /* 15 characters + 1 byte alignement*/
#define mcsCMD_LEN              8   /* max. length of a command name    */
#define mcsUNKNOWN_PROC "unknown"   /* name used for unknown processes  */
#define mcsUNKNOWN_ENV  "none"      /* name used for unknown environment*/

/************************************************************************
 *                          MCS   Data  Types                           *
 ************************************************************************/
typedef char               mcsINT8;       /*  8 bits integers           */
typedef unsigned char      mcsUINT8;      /*  8 bits unsigned integers  */
typedef short              mcsINT16;      /* 16 bits integers           */
typedef unsigned short     mcsUINT16;     /* 16 bits unsigned integers  */
typedef int                mcsINT32;      /* 32 bits integers           */
typedef unsigned int       mcsUINT32;     /* 32 bits unsigned integers  */
typedef double             mcsDOUBLE;    
typedef float              mcsFLOAT;

typedef unsigned char      mcsBYTES4[4]; 
typedef unsigned char      mcsBYTES8[8]; 
typedef unsigned char      mcsBYTES12[12]; 
typedef unsigned char      mcsBYTES16[16]; 
typedef unsigned char      mcsBYTES20[20]; 
typedef unsigned char      mcsBYTES32[32]; 
typedef unsigned char      mcsBYTES48[48]; 
typedef unsigned char      mcsBYTES64[64]; 
typedef unsigned char      mcsBYTES80[80]; 
typedef unsigned char      mcsBYTES128[128]; 
typedef unsigned char      mcsBYTES256[256]; 

typedef char               mcsSTRING4[4]; 
typedef char               mcsSTRING8[8]; 
typedef char               mcsSTRING12[12]; 
typedef char               mcsSTRING16[16]; 
typedef char               mcsSTRING20[20]; 
typedef char               mcsSTRING32[32]; 
typedef char               mcsSTRING48[48]; 
typedef char               mcsSTRING64[64]; 
typedef char               mcsSTRING80[80]; 
typedef char               mcsSTRING128[128]; 
typedef char               mcsSTRING256[256];

typedef char mcsPROCNAME[mcsPROCNAME_LEN+1];   /* Process name          */
typedef char mcsENVNAME[mcsENVNAME_LEN+1];     /* Environnement name    */
typedef char mcsMODULEID[mcsMODULEID_LEN+1];   /* Software module name  */
typedef char mcsFILE_LINE[64];                 /* File/line information */
typedef char mcsCMD[mcsCMD_LEN+1];             /* Command name          */

#define mcsNULL_CMD  ""

/*
 *   Definition of logical  
 */
typedef enum 
{
    mcsFALSE = 0,   /* False Logical */
    mcsTRUE  = 1    /* True Logical  */
} mcsLOGICAL;     

/*
 *   Definition of the routine completion status
 */
typedef enum 
{
    FAILURE = 1,
    SUCCESS 
} mcsCOMPL_STAT;       /* Completion status returned by subroutines */

/*
 * Public functions
 */
mcsCOMPL_STAT mcsInit(const mcsPROCNAME  procName);
const char *mcsGetProcName();
const char *mcsGetEnvName();
void mcsExit();

/*
 * Convenience macros
 */
#define mcsMAX(a,b)  ((a)>=(b)?(a):(b))
#define mcsMIN(a,b)  ((a)<=(b)?(a):(b))

#ifndef __FILE_LINE__
#define mcsIToStr(a) #a
#define mcsIToStr2(a) mcsIToStr(a) 
#define __FILE_LINE__ __FILE__ ":" mcsIToStr2(__LINE__)
#endif /*!__FILE_LINE__*/

#ifdef __cplusplus
}
#endif

#endif /*!MCS_H*/
