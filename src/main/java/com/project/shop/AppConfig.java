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
            {"Elegant Twist", "A stylish piece with a modern twist.", "8789"},
            {"Golden Curve", "Subtle curves with a classic finish.", "9350"},
            {"Vintage Loop", "Timeless design for any occasion.", "8580"},
            {"Pearl Whisper", "Soft tones and smooth texture.", "9790"},
            {"Braided Shine", "A glowing braid-inspired design.", "9020"},
            {"Charm Drop", "Minimalist with a subtle charm drop.", "8470"},
            {"Bold Link", "Chunky links for a bold statement.", "10560"},
            {"Rose Fade", "Soft pink hue with a glossy finish.", "9240"},
            {"Midnight Metal", "Dark tones perfect for the evening.", "10010"},
            {"Simple Lock", "Clean design with a secure lock.", "8030"},
            {"Dual Strand", "Two delicate strands paired together.", "9680"},
            {"Ocean Breeze", "Flowy design with light curves.", "8690"},
            {"Sunlit Edge", "Bright, reflective edge detailing.", "9295"},
            {"Hidden Gem", "Tiny gems embedded subtly.", "10285"},
            {"Woven Gold", "Fabric-inspired weaving in gold.", "9790"},
            {"Nature Band", "Leaf patterns with a matte finish.", "9460"},
            {"Crystal Line", "Tiny crystals line the center.", "10120"},
            {"Sleek Wrap", "Wrap-style minimalist bracelet.", "8140"},
            {"Tide Motion", "Flowing form that mimics waves.", "8855"},
            {"Antique Bound", "Classic aged-metal look.", "10670"},
            {"Star Dust", "Tiny sparkles with a dreamy glow.", "9350"},
            {"Geo Cut", "Geometric patterns etched in.", "9680"},
            {"Mirror Polish", "High gloss, ultra-reflective look.", "9845"},
            {"Petal Band", "Petal shapes circling the band.", "8415"}
    };
    public static String[][] earrings = {
            {"Golden Drop", "Elegant teardrop style for evenings.", "10725"},
            {"Crystal Speck", "Small and shiny—easy everyday wear.", "8932"},
            {"Classic Loop", "Timeless loop design in clean metal.", "9100"},
            {"Feather Flick", "Lightweight with feather-like curves.", "8740"},
            {"Spark Shine", "Studded stones with maximum sparkle.", "9795"},
            {"Orb Glow", "Polished orbs that catch the light.", "9260"},
            {"Twist Stud", "Subtle twist in a compact stud.", "8990"},
            {"Vintage Drape", "Dangling with an old-world charm.", "10560"},
            {"Moonlit Edge", "Sharp and subtle half-moon cuts.", "9885"},
            {"Rippled Metal", "Soft waves for a fluid look.", "8610"},
            {"Tiny Hoops", "Minimal hoops for all-day wear.", "8030"},
            {"Gem Dot", "Single centered gem design.", "8488"},
            {"Star Curve", "Curved design mimicking constellations.", "9200"},
            {"Cloud Stone", "Soft hue and cloudy finish.", "8910"},
            {"Geo Stud", "Flat geometric studs.", "8405"},
            {"Pearl Edge", "Half-pearl in clean setting.", "9425"},
            {"Satin Flow", "Sleek and slightly flowing metal.", "9670"},
            {"Petal Glow", "Petal shape with shimmer overlay.", "9940"},
            {"Stone Fan", "Layered like a fan of stones.", "10170"},
            {"Thread Drop", "Long thread style earring drop.", "9850"},
            {"Chic Swirl", "Simple but bold swirl.", "9025"},
            {"Mirror Glint", "High-polished surface with reflection.", "9600"},
            {"Halo Ring", "Circular halo design.", "9450"},
            {"Mini Twist", "Compact with soft twists.", "8735"}
    };
    public static String[][] necklaces = {
            {"Infinity Line", "Endless shape symbolizing eternity.", "10890"},
            {"Soft Chain", "Smooth and silky link design.", "9425"},
            {"Sun Charm", "Minimal chain with sun pendant.", "9980"},
            {"Teardrop Glow", "Pendant with droplet shape.", "9675"},
            {"Bold Chain", "Thicker chain for strong impact.", "11110"},
            {"Rose Shine", "Rose-gold finish with subtle shine.", "9650"},
            {"Layered Grace", "Two-layered necklace for depth.", "10025"},
            {"Sky Loop", "Looped pendant with open center.", "9580"},
            {"Star Path", "Stars strung in a gentle curve.", "9900"},
            {"Wave Bar", "Horizontal bar shaped like a wave.", "9055"},
            {"Crystal Beam", "Slim crystal-lined pendant.", "10275"},
            {"Feather Chain", "Light design resembling feathers.", "9740"},
            {"Lock Pendant", "Small lock charm centerpiece.", "9875"},
            {"Circle Fade", "Gradient-toned round pendant.", "10150"},
            {"Gemstone Bite", "Tiny gemstone tucked in.", "9735"},
            {"Knot Loop", "Knot-style loop pendant.", "9370"},
            {"Pendant Swing", "Movable charm with motion.", "9650"},
            {"Split Ring", "Ring split with gem line.", "9915"},
            {"Midnight Dot", "Dark tone with a single stone.", "10300"},
            {"Glass Stone", "Transparent pendant with luster.", "9200"},
            {"Mirror Plate", "Flat and polished disk pendant.", "9885"},
            {"Layer Curve", "Double-layered chain with curves.", "10560"},
            {"Pendant Beam", "Elongated charm drop.", "10050"},
            {"Blush Metal", "Warm tone with metallic sheen.", "9450"}
    };
    public static String[][] rings = {
            {"Bold Band", "Wide surface with minimal design.", "9985"},
            {"Twist Pair", "Double band intertwined together.", "9600"},
            {"Stone Tip", "Single gemstone offset to one side.", "10500"},
            {"Floral Curve", "Small flower etchings on surface.", "9870"},
            {"Mirror Edge", "Reflective rim with gloss finish.", "9340"},
            {"Criss Cross", "Two bands crossing paths.", "9845"},
            {"Matte Gold", "Understated matte texture.", "9100"},
            {"Petal Stone", "Small petals around a gem.", "10010"},
            {"Slit Band", "Slight slit opening in the front.", "9420"},
            {"Orb Ring", "Sphere centerpiece in gold.", "9275"},
            {"Soft Spiral", "Gentle spiral wrapping design.", "8730"},
            {"Halo Set", "Encircled gemstone at center.", "10560"},
            {"Etched Line", "One engraved line around.", "8995"},
            {"Stack Ring", "Designed to be worn stacked.", "9655"},
            {"Tide Ring", "Wavy pattern inspired by water.", "9840"},
            {"Minimal Dot", "Just one tiny central dot.", "8595"},
            {"Retro Crest", "Old-style seal ring reimagined.", "10120"},
            {"Gem Cut", "Sharp cut edges on gemstone.", "9805"},
            {"Mesh Look", "Ring mimics a metal mesh.", "9475"},
            {"Cloud Twist", "Pale with cloudy swirl.", "9580"},
            {"Lined Band", "Several lines wrapping the band.", "9860"},
            {"Stone Drop", "Drop-shaped gem detail.", "10040"},
            {"Grain Texture", "Sandy texture surface.", "9110"},
            {"Mirror Dome", "Dome-shaped with mirror polish.", "10390"}
    };

}
