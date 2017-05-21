/**
 * MegaMek - Copyright (C) 2005 Ben Mazur (bmazur@sev.org)
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 2 of the License, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 */
package megamek.common.weapons.battlearmor;

import megamek.common.TechAdvancement;
import megamek.common.weapons.missiles.MRMWeapon;


/**
 * @author Sebastian Brocks
 */
public class ISBAMRM3OS extends MRMWeapon {

    /**
     *
     */
    private static final long serialVersionUID = -2684723412113127349L;

    /**
     *
     */
    public ISBAMRM3OS() {
        super();
        name = "MRM 3 (OS)";
        setInternalName("ISBAMRM3OS");
        addLookupName("IS BA MRM3 OS");
        rackSize = 3;
        shortRange = 3;
        mediumRange = 8;
        longRange = 15;
        extremeRange = 16;
        bv = 4;
        cost = 7500;
        tonnage = .15;
        criticals = 3;
        flags = flags.or(F_NO_FIRES).or(F_BA_WEAPON).or(F_ONESHOT).andNot(F_MECH_WEAPON).andNot(F_TANK_WEAPON).andNot(F_AERO_WEAPON).andNot(F_PROTO_WEAPON);
        rulesRefs = "261, TM";

        techAdvancement.setTechBase(TechAdvancement.TECH_BASE_IS);
        techAdvancement.setISAdvancement(3053, 3060, 3067);
        techAdvancement.setTechRating(RATING_D);
        techAdvancement.setAvailability( new int[] { RATING_X, RATING_X, RATING_D, RATING_D });
    }
}
