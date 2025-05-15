package com.polansky.amino

/**
 * Hardcoded daily minimum intake per amino acid, in milligrams (mg).
 */
object DailyMinimumIntake {
    var minimumMgPerAmino: Map<AminoAcid, Double> = mapOf(
        AminoAcid.HISTIDINE to 700.0,
        AminoAcid.ISOLEUCINE to 1_400.0,
        AminoAcid.LEUCINE to 2_730.0,
        AminoAcid.LYSINE to 2_100.0,
        AminoAcid.METHIONINE to 700.0,
        AminoAcid.THREONINE to 1_050.0,
        AminoAcid.TRYPTOPHAN to 280.0,
        AminoAcid.VALINE to 1_820.0,
        AminoAcid.TRYPTOPHAN to 280.0,
        AminoAcid.PROTEIN to 55_000.0,
        //https://www.nal.usda.gov/human-nutrition-and-food-safety/dri-calculator/results
        //https://www.health.harvard.edu/blog/how-much-protein-do-you-need-every-day-201506188096
        //65 kg * 0.85 (0.8 je min, 0.85 je buffer)
    )
}