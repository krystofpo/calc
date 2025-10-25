package com.polansky.amino

/**
 * Hardcoded daily minimum intake per amino acid, in milligrams (mg).
 */
object DailyMinimumIntake {
    val minimumMgPerAmino: Map<AminoAcid, Double> = mapOf(

        //for amino acids, the source is
        //https://iris.who.int/server/api/core/bitstreams/b7c5ec43-bc59-4b38-b702-3f0e96a06fa1/content page 163/284, table 23
        // i take 65 kg and calculate the minimum and add 10 per cent safety buffer
        AminoAcid.HISTIDINE to 715.0,
        AminoAcid.ISOLEUCINE to 1_430.0,
        AminoAcid.LEUCINE to 2_780.0,
        AminoAcid.LYSINE to 2_100.0,
        AminoAcid.METHIONINE to 710.0,
        AminoAcid.THREONINE to 1_070.0,
        AminoAcid.TRYPTOPHAN to 280.0,
        AminoAcid.VALINE to 1_850.0,
        AminoAcid.PHENYLALANINE to 1_780.0,
        AminoAcid.PROTEIN to 57_000.0,
        //for proteein
        //https://www.nal.usda.gov/human-nutrition-and-food-safety/dri-calculator/results
        //https://www.health.harvard.edu/blog/how-much-protein-do-you-need-every-day-201506188096
        //65 kg * 0.8  * 1.1  (0.8 je min, 1.1 je buffer)
    )
}