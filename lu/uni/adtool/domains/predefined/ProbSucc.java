/**
 * Author: Piotr Kordy (piotr.kordy@uni.lu <mailto:piotr.kordy@uni.lu>)
 * Date:   06/06/2013
 * Copyright (c) 2013,2012 University of Luxembourg -- Faculty of Science,
 *     Technology and Communication FSTC
 * All rights reserved.
 * Licensed under GNU Affero General Public License 3.0;
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Affero General Public License as
 *    published by the Free Software Foundation, either version 3 of the
 *    License, or (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package lu.uni.adtool.domains.predefined;

import lu.uni.adtool.domains.Domain;

import lu.uni.adtool.domains.rings.RealZeroOne;

/**
 * A Domain defined on booleans.
 * 
 * @author Piot Kordy
 */
public class ProbSucc implements Domain<RealZeroOne>
{
  //number 8
  static final long serialVersionUID = 865945232556446848L;
  /**
   * Constructs a new instance.
   */
  public ProbSucc()
  {
	  super();
  }

  /**
   * {@inheritDoc}
   * @see Domain#getDefaultValue()
   */
  public final RealZeroOne getDefaultValue(final boolean proponent)
  {
    if  (proponent){
      return new RealZeroOne(0);
    }
    else{
      return new RealZeroOne(0);
    }
  }
  /**
   * {@inheritDoc}
   * @see Domain#isValueModifiable(boolean)
   */
  public final boolean isValueModifiable(final boolean proponent)
  {
    return true;
  }
  /**
   * {@inheritDoc}
   * @see Domain#getName()
   */
  public final String getName()
  {
	
    return "Probability of success";
  }
  /**
   * {@inheritDoc}
   * @see Domain#getDescription()
   */

  public final String getDescription()
  {
    final String name = "Probability of success, assuming "
      + "that all actions are mutually independent";
    final String vd = "[0,1]";
    final String[] operators = {"<i>x</i>&nbsp;+&nbsp;<i>y</i>&nbsp;-&nbsp;<i>x</i><i>y</i>",
                              "<i>x</i><i>y</i>",
                              "<i>x</i>&nbsp;+&nbsp;<i>y</i>&nbsp;-&nbsp;<i>x</i><i>y</i>",
                              "<i>x</i><i>y</i>",
                              "<i>x</i>(1&nbsp;-&nbsp;<i>y</i>)",
                              "<i>x</i>(1&nbsp;-&nbsp;<i>y</i>)"};
    return DescriptionGenerator.generateDescription(this, name, vd, operators);
  }

 
  /**
   * {@inheritDoc}
   * @see Domain#op(RealZeroOne,RealZeroOne)
   */
  public final RealZeroOne op(final RealZeroOne a, final RealZeroOne b)
  {
    return RealZeroOne.plusProb(a,b);
  }

  /**
   * {@inheritDoc}
   * @see Domain#ap(RealZeroOne,RealZeroOne)
   */
  public final RealZeroOne ap(final RealZeroOne a, final RealZeroOne b)
  {
    return RealZeroOne.times(a,b);
  }

  /**
   * {@inheritDoc}
   * @see Domain#oo(RealZeroOne,RealZeroOne)
   */
  public final RealZeroOne oo(final RealZeroOne a,final  RealZeroOne b)
  {
    return RealZeroOne.plusProb(a,b);
  }

  /**
   * {@inheritDoc}
   * @see Domain#ao(RealZeroOne,RealZeroOne)
   */
  public final RealZeroOne ao(final RealZeroOne a, final RealZeroOne b)
  {
    return RealZeroOne.times(a,b);
  }

  /**
   * {@inheritDoc}
   * @see Domain#cp(RealZeroOne,RealZeroOne)
   */
  public final RealZeroOne cp(final RealZeroOne a,final  RealZeroOne b)
  {
    return RealZeroOne.minusProb(a,b);
  }

  /**
   * {@inheritDoc}
   * @see Domain#co(RealZeroOne,RealZeroOne)
   */
  public final RealZeroOne co(final RealZeroOne a, final RealZeroOne b)
  {
    return RealZeroOne.minusProb(a,b);
  }
}



