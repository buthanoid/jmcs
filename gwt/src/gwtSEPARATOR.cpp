/*******************************************************************************
 * JMMC project ( http://www.jmmc.fr ) - Copyright (C) CNRS.
 ******************************************************************************/

/**
 * \file
 * Definition of gwtSEPARATOR class.
 */


/* 
 * System Headers 
 */
#include <iostream>
using namespace std;


/*
 * MCS Headers 
 */
#include "mcs.h"
#include "log.h"
#include "err.h"


/*
 * Local Headers 
 */
#include "gwtSEPARATOR.h"
#include "gwtPrivate.h"


/*
 * Class constructor
 */

/** 
 * Constructs the widget.
 */
gwtSEPARATOR::gwtSEPARATOR()
{
    logExtDbg("gwtSEPARATOR::gwtSEPARATOR()");
}

/*
 * Class destructor
 */
gwtSEPARATOR::~gwtSEPARATOR()
{
    logExtDbg("gwtSEPARATOR::~gwtSEPARATOR()");
}

/*
 * Public methods
 */

string gwtSEPARATOR::GetXmlBlock()
{
    logExtDbg("gwtSEPARATOR::GetXmlBlock()");
    string s("<SEPARATOR ");
    AppendXmlAttributes(s);
    s.append("/>");
    return s;
}

/*
 * Protected methods
 */



/*
 * Private methods
 */



/*___oOo___*/