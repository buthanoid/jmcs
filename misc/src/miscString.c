/*******************************************************************************
* JMMC project
*
* who       when		 what
* --------  -----------	 -------------------------------------------------------
* gzins     16-Jun-2004  created
*
*-----------------------------------------------------------------------------*/

static char *rcsId="@(#) $Id: miscString.c,v 1.3 2004-06-17 15:04:40 lafrasse Exp $";
static void *use_rcsId = ((void)&use_rcsId,(void *) &rcsId);

/*
 * System Headers
 */
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include <sys/time.h>
#include <ctype.h>

/*
 * MCS Headers
 */
#include "mcs.h"

/*
 * Local Headers
 */
#include "misc.h"

/**
 * Strip quotes enclosing a string.
 *
 * Strings are sometimes enclosed in quotes. This function strips these off
 * using the same character buffer for storing the processed string. The
 * string must be NULL terminated.
 *
 * If the string is not contained in quotes, the function simply returns
 * without changing the string.
 *
 * /param string that shall be stripped.
 */
void miscStripQuotes(char *string)
{
    char  *srcPtr;
    char  *dstPtr;

    /* Worst-case string which becomes:
     *   |   "   kjkdjd kjkjk   kjkj  "  ;   |
     *   |kjkdjd kjkjk   kjkj|
     */

    dstPtr = string;
    /*= Find first '"' */
    srcPtr = strchr(string, '\"');

    /*= If a quote has been found '"' */
    if (srcPtr != NULL)
    {
        /* Skip possible blanks after the quote */
        srcPtr++;
        while (*srcPtr == ' ')
        {
            srcPtr++;
        }

        /* Copy until quote or NULL terminator */
        while ((*srcPtr != '\0') && (*srcPtr != '\"')) /* " */
        {
            *dstPtr = *srcPtr;
            dstPtr++;
            srcPtr++;
        }

        /* If the string only contains blanks or is of length 0, dstPtr still
         * points to the beginning of the string
         */

        /* Yank possible trailing blanks */
        if (dstPtr != string)
        {
            dstPtr--;
            while (*dstPtr == ' ')
                dstPtr--;
            dstPtr++;
        }
        *dstPtr = '\0';
    }
}

/**
 * Convert a string to upper case.
 *
 * Strings are sometimes composed of mixed caracaters case. This function
 * cleans this by upper-casing all the caracters, using the same character
 * buffer for storing the processed string. The string must be NULL terminated.
 *
 * /param string that shall be upper-cased.
 */
void miscStrToUpper(char *string)
{
    /* Worst-case string which becomes:
     *   |Abc deF GhI jKl 012 .;/|
     *   |ABC DEF GHI JKL 012 .;/|
     */

    while (*string != '\0')
    {
        *string = toupper(*string);
        *string++;
    }
}

/*___oOo___*/
