package com.project.shop;

import java.sql.Connection;

public class AppConfig {
    public static Connection connection = null;
    public static int user = 0;

    public static String userName;
    public static String userAddress;
    public static String userEmail;
    public static String userPhone;

    public static int getCurrentUser() {
        return user;
    }
    public static String[][] bracelets = {
            {"Elegant Twist", "A stylish piece with a modern twist.", "8789", "58"},
            {"Golden Curve", "Subtle curves with a classic finish.", "9350", "34"},
            {"Vintage Loop", "Timeless design for any occasion.", "8580", "72"},
            {"Pearl Whisper", "Soft tones and smooth texture.", "9790", "41"},
            {"Braided Shine", "A glowing braid-inspired design.", "9020", "65"},
            {"Charm Drop", "Minimalist with a subtle charm drop.", "8470", "88"},
            {"Bold Link", "Chunky links for a bold statement.", "10560", "29"},
            {"Rose Fade", "Soft pink hue with a glossy finish.", "9240", "53"},
            {"Midnight Metal", "Dark tones perfect for the evening.", "10010", "37"},
            {"Simple Lock", "Clean design with a secure lock.", "8030", "95"},
            {"Dual Strand", "Two delicate strands paired together.", "9680", "48"},
            {"Ocean Breeze", "Flowy design with light curves.", "8690", "77"},
            {"Sunlit Edge", "Bright, reflective edge detailing.", "9295", "61"},
            {"Hidden Gem", "Tiny gems embedded subtly.", "10285", "31"},
            {"Woven Gold", "Fabric-inspired weaving in gold.", "9790", "45"},
            {"Nature Band", "Leaf patterns with a matte finish.", "9460", "69"},
            {"Crystal Line", "Tiny crystals line the center.", "10120", "39"},
            {"Sleek Wrap", "Wrap-style minimalist bracelet.", "8140", "91"},
            {"Tide Motion", "Flowing form that mimics waves.", "8855", "74"},
            {"Antique Bound", "Classic aged-metal look.", "10670", "25"},
            {"Star Dust", "Tiny sparkles with a dreamy glow.", "9350", "55"},
            {"Geo Cut", "Geometric patterns etched in.", "9680", "49"},
            {"Mirror Polish", "High gloss, ultra-reflective look.", "9845", "51"},
            {"Petal Band", "Petal shapes circling the band.", "8415", "82"}
    };

    public static String[][] earrings = {
            {"Golden Drop", "Elegant teardrop style for evenings.", "10725", "42"},
            {"Crystal Speck", "Small and shiny—easy everyday wear.", "8932", "110"},
            {"Classic Loop", "Timeless loop design in clean metal.", "9100", "85"},
            {"Feather Flick", "Lightweight with feather-like curves.", "8740", "93"},
            {"Spark Shine", "Studded stones with maximum sparkle.", "9795", "68"},
            {"Orb Glow", "Polished orbs that catch the light.", "9260", "78"},
            {"Twist Stud", "Subtle twist in a compact stud.", "8990", "105"},
            {"Vintage Drape", "Dangling with an old-world charm.", "10560", "49"},
            {"Moonlit Edge", "Sharp and subtle half-moon cuts.", "9885", "62"},
            {"Rippled Metal", "Soft waves for a fluid look.", "8610", "99"},
            {"Tiny Hoops", "Minimal hoops for all-day wear.", "8030", "120"},
            {"Gem Dot", "Single centered gem design.", "8488", "115"},
            {"Star Curve", "Curved design mimicking constellations.", "9200", "81"},
            {"Cloud Stone", "Soft hue and cloudy finish.", "8910", "88"},
            {"Geo Stud", "Flat geometric studs.", "8405", "118"},
            {"Pearl Edge", "Half-pearl in clean setting.", "9425", "75"},
            {"Satin Flow", "Sleek and slightly flowing metal.", "9670", "71"},
            {"Petal Glow", "Petal shape with shimmer overlay.", "9940", "59"},
            {"Stone Fan", "Layered like a fan of stones.", "10170", "53"},
            {"Thread Drop", "Long thread style earring drop.", "9850", "65"},
            {"Chic Swirl", "Simple but bold swirl.", "9025", "83"},
            {"Mirror Glint", "High-polished surface with reflection.", "9600", "74"},
            {"Halo Ring", "Circular halo design.", "9450", "77"},
            {"Mini Twist", "Compact with soft twists.", "8735", "95"}
    };

    public static String[][] necklaces = {
            {"Infinity Line", "Endless shape symbolizing eternity.", "10890", "33"},
            {"Soft Chain", "Smooth and silky link design.", "9425", "67"},
            {"Sun Charm", "Minimal chain with sun pendant.", "9980", "52"},
            {"Teardrop Glow", "Pendant with droplet shape.", "9675", "61"},
            {"Bold Chain", "Thicker chain for strong impact.", "11110", "28"},
            {"Rose Shine", "Rose-gold finish with subtle shine.", "9650", "64"},
            {"Layered Grace", "Two-layered necklace for depth.", "10025", "47"},
            {"Sky Loop", "Looped pendant with open center.", "9580", "66"},
            {"Star Path", "Stars strung in a gentle curve.", "9900", "55"},
            {"Wave Bar", "Horizontal bar shaped like a wave.", "9055", "79"},
            {"Crystal Beam", "Slim crystal-lined pendant.", "10275", "44"},
            {"Feather Chain", "Light design resembling feathers.", "9740", "59"},
            {"Lock Pendant", "Small lock charm centerpiece.", "9875", "57"},
            {"Circle Fade", "Gradient-toned round pendant.", "10150", "49"},
            {"Gemstone Bite", "Tiny gemstone tucked in.", "9735", "60"},
            {"Knot Loop", "Knot-style loop pendant.", "9370", "71"},
            {"Pendant Swing", "Movable charm with motion.", "9650", "63"},
            {"Split Ring", "Ring split with gem line.", "9915", "56"},
            {"Midnight Dot", "Dark tone with a single stone.", "10300", "41"},
            {"Glass Stone", "Transparent pendant with luster.", "9200", "75"},
            {"Mirror Plate", "Flat and polished disk pendant.", "9885", "58"},
            {"Layer Curve", "Double-layered chain with curves.", "10560", "38"},
            {"Pendant Beam", "Elongated charm drop.", "10050", "50"},
            {"Blush Metal", "Warm tone with metallic sheen.", "9450", "68"}
    };

    public static String[][] rings = {
            {"Bold Band", "Wide surface with minimal design.", "9985", "40"},
            {"Twist Pair", "Double band intertwined together.", "9600", "55"},
            {"Stone Tip", "Single gemstone offset to one side.", "10500", "32"},
            {"Floral Curve", "Small flower etchings on surface.", "9870", "48"},
            {"Mirror Edge", "Reflective rim with gloss finish.", "9340", "62"},
            {"Criss Cross", "Two bands crossing paths.", "9845", "50"},
            {"Matte Gold", "Understated matte texture.", "9100", "70"},
            {"Petal Stone", "Small petals around a gem.", "10010", "45"},
            {"Slit Band", "Slight slit opening in the front.", "9420", "58"},
            {"Orb Ring", "Sphere centerpiece in gold.", "9275", "65"},
            {"Soft Spiral", "Gentle spiral wrapping design.", "8730", "78"},
            {"Halo Set", "Encircled gemstone at center.", "10560", "30"},
            {"Etched Line", "One engraved line around.", "8995", "75"},
            {"Stack Ring", "Designed to be worn stacked.", "9655", "53"},
            {"Tide Ring", "Wavy pattern inspired by water.", "9840", "51"},
            {"Minimal Dot", "Just one tiny central dot.", "8595", "80"},
            {"Retro Crest", "Old-style seal ring reimagined.", "10120", "43"},
            {"Gem Cut", "Sharp cut edges on gemstone.", "9805", "49"},
            {"Mesh Look", "Ring mimics a metal mesh.", "9475", "57"},
            {"Cloud Twist", "Pale with cloudy swirl.", "9580", "56"},
            {"Lined Band", "Several lines wrapping the band.", "9860", "47"},
            {"Stone Drop", "Drop-shaped gem detail.", "10040", "46"},
            {"Grain Texture", "Sandy texture surface.", "9110", "68"},
            {"Mirror Dome", "Dome-shaped with mirror polish.", "10390", "35"}
    };
}
