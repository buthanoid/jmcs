/*******************************************************************************
 * JMMC project ( http://www.jmmc.fr ) - Copyright (C) CNRS.
 ******************************************************************************/

/**
 * \file
 * Definition of gwtCELL class.
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
#include "gwtCELL.h"
#include "gwtPrivate.h"

/**
 * User should not use this to build a gwtTABLE. Initialize a new Cell content.
 * \param textContent The text content of the cell.
 */
gwtCELL::gwtCELL(string textContent)
{
    logExtDbg("gwtCELL::gwtCELL()");
    _textContent = textContent;
}

/**
 * Class destructor
 */
gwtCELL::~gwtCELL()
{
    logExtDbg("gwtCELL::~gwtCELL()");
}

/*
 * Public methods
 */

/** 
 * Set the content of the cell.
 * \param content the content to be assigned.
 */
void gwtCELL::SetContent(string content)
{
    logExtDbg("gwtCELL::SetContent()");
    _textContent=content;
}

/** 
 * Get the content of the cell.
 * \return the content of the cell
 */
string gwtCELL::GetContent()
{
    logExtDbg("gwtCELL::GetContent()");
    return _textContent;
}

/** 
 * Set the background color of the cell.
 * \param bgcolor the background color to be assigned.
 */
void gwtCELL::SetBackgroundColor(string bgcolor)
{
    logExtDbg("gwtCELL::SetBackgroundColor()");
    _backgroundColor=bgcolor;
}

/** 
 * Returns the corresponding xml block of this cell.
 * \return the xml string representation.
 */
string gwtCELL::GetXmlBlock()
{
    logExtDbg("gwtCELL::GetXmlBlock()");
    string xmlStr;
    if(_backgroundColor.empty())
    {
        xmlStr.append("<TD>");
    }else{
        xmlStr.append("<TD bgcolor=\"");
        xmlStr.append(_backgroundColor);
        xmlStr.append("\">");
    }
    xmlStr.append(_textContent);
    xmlStr.append("</TD>");
  
    return xmlStr;
}

/*
 * Protected methods
 */


/*
 * Private methods
 */


/*___oOo___*/
