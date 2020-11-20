package com.metalichecky.amonguseditor.repo

import com.metalichecky.amonguseditor.model.item.Hat
import com.metalichecky.amonguseditor.model.item.Item
import com.metalichecky.amonguseditor.model.item.Pet
import com.metalichecky.amonguseditor.model.item.Skin
import kotlin.math.absoluteValue
import kotlin.random.Random
import kotlin.reflect.KClass

object ItemsRepo {
    private val random = Random(System.currentTimeMillis())

    fun getHats(): List<Hat> {
        return hats
    }

    fun getSkins(): List<Skin> {
        return skins
    }

    fun getPets(): List<Pet> {
        return pets
    }

    private var hatsRandom: MutableList<Hat> = mutableListOf()
    private var petsRandom: MutableList<Pet> = mutableListOf()
    private var skinsRandom: MutableList<Skin> = mutableListOf()

    fun <T: Any> getRandomNonRepeatable(clazz: KClass<T>): Item? {
        println("getRandomItem() ${clazz} ${Hat::class} ${clazz == Hat::class}")
        if (hatsRandom.isEmpty()) {
            hatsRandom = getHats().toMutableList()
        }
        if (petsRandom.isEmpty()) {
            petsRandom = getPets().toMutableList()
        }
        if (skinsRandom.isEmpty()) {
            skinsRandom = getSkins().toMutableList()
        }
        return when(clazz) {
            Hat::class -> {
                val hat = hatsRandom.random(random)
                hatsRandom.remove(hat)
                hat
            }
            Skin::class -> {
                val skin = skinsRandom.random(random)
                skinsRandom.remove(skin)
                skin
            }
            Pet::class -> {
                val pet = petsRandom.random(random)
                petsRandom.remove(pet)
                pet
            }
            else -> null
        }
    }

    fun <T: Any> getRandomItem(clazz: KClass<T>): Item? {
        println("getRandomItem() ${clazz} ${Hat::class} ${clazz == Hat::class}")
        return when(clazz) {
            Hat::class -> {
                getHats().random(random)
            }
            Skin::class -> {
                getSkins().random(random)
            }
            Pet::class -> {
                getPets().random(random)
            }
            else -> null
        }
    }
}

val hats = listOf(
    Hat(0, "None", "hat0000.png"),
    Hat(
        1,
        "Astronaut Helmet",
        "hat0001.png"
    ),
    Hat(
        2,
        "Backwards Cap",
        "hat0002.png"
    ),
    Hat(
        3,
        "Brain Slug",
        "hat0003.png"
    ),
    Hat(4, "Bush Hat", "hat0004.png"),
    Hat(
        5,
        "Captain Hat",
        "hat0005.png"
    ),
    Hat(
        6,
        "Double Top Hat",
        "hat0006.png"
    ),
    Hat(
        7,
        "Flowerpot Hat",
        "hat0007.png"
    ),
    Hat(8, "Goggles", "hat0008.png"),
    Hat(9, "Hard Hat", "hat0009.png"),
    Hat(
        10,
        "Military Hat",
        "hat0010.png"
    ),
    Hat(
        11,
        "Paper Hat",
        "hat0011.png"
    ),
    Hat(
        12,
        "Party Hat",
        "hat0012.png"
    ),
    Hat(
        13,
        "Police Hat",
        "hat0013.png"
    ),
    Hat(
        14,
        "Stethoscope",
        "hat0014.png"
    ),
    Hat(15, "Top Hat", "hat0015.png"),
    Hat(
        16,
        "Towel Wizard",
        "hat0016.png"
    ),
    Hat(17, "Ushanka", "hat0017.png"),
    Hat(18, "Viking", "hat0018.png"),
    Hat(
        19,
        "Wall Guard Cap",
        "hat0019.png"
    ),
    Hat(20, "Snowman", "hat0020.png"),
    Hat(21, "Antlers", "hat0021.png"),
    Hat(
        22,
        "Christmas Lights Hat",
        "hat0022.png"
    ),
    Hat(
        23,
        "Santa Hat",
        "hat0023.png"
    ),
    Hat(
        24,
        "Christmas Tree Hat",
        "hat0024.png"
    ),
    Hat(
        25,
        "Present Hat",
        "hat0025.png"
    ),
    Hat(
        26,
        "Candy Canes Hat",
        "hat0026.png"
    ),
    Hat(27, "Elf Hat", "hat0027.png"),
    Hat(
        28,
        "2019 Yellow Party Hat",
        "hat0028.png"
    ),
    Hat(
        29,
        "White Hat",
        "hat0029.png"
    ),
    Hat(30, "Crown", "hat0030.png"),
    Hat(31, "Eyebrows", "hat0031.png"),
    Hat(
        32,
        "Angel Halo",
        "hat0032.png"
    ),
    Hat(33, "Elf Cap", "hat0033.png"),
    Hat(34, "Flat Cap", "hat0034.png"),
    Hat(35, "Plunger", "hat0035.png"),
    Hat(36, "Snorkel", "hat0036.png"),
    Hat(
        37,
        "Stickmin Figure",
        "hat0037.png"
    ),
    Hat(
        38,
        "Straw Hat",
        "hat0038.png"
    ),
    Hat(
        39,
        "Sheriff Hat",
        "hat0039.png"
    ),
    Hat(
        40,
        "Eyeball Lamp",
        "hat0040.png"
    ),
    Hat(
        41,
        "Toilet Paper Hat",
        "hat0041.png"
    ),
    Hat(
        42,
        "Toppat Clan Leader Hat",
        "hat0042.png"
    ),
    Hat(
        43,
        "Black Fedora",
        "hat0043.png"
    ),
    Hat(
        44,
        "Ski Goggles",
        "hat0044.png"
    ),
    Hat(
        45,
        "MIRA Landing Headset",
        "hat0045.png"
    ),
    Hat(
        46,
        "MIRA Hazmat Mask",
        "hat0046.png"
    ),
    Hat(
        47,
        "Medical Mask",
        "hat0047.png"
    ),
    Hat(
        48,
        "MIRA Security Cap",
        "hat0048.png"
    ),
    Hat(
        49,
        "Safari Hat",
        "hat0049.png"
    ),
    Hat(
        50,
        "Banana Hat",
        "hat0050.png"
    ),
    Hat(51, "Beanie", "hat0051.png"),
    Hat(
        52,
        "Bear Ears",
        "hat0052.png"
    ),
    Hat(
        53,
        "Cheese Hat",
        "hat0053.png"
    ),
    Hat(
        54,
        "Cherry Hat",
        "hat0054.png"
    ),
    Hat(55, "Egg Hat", "hat0055.png"),
    Hat(
        56,
        "Green Fedora",
        "hat0056.png"
    ),
    Hat(
        57,
        "Flamingo Hat",
        "hat0057.png"
    ),
    Hat(
        58,
        "Flower Hat",
        "hat0058.png"
    ),
    Hat(
        59,
        "Knight Helmet",
        "hat0059.png"
    ),
    Hat(
        60,
        "Plant Hat",
        "hat0060.png"
    ),
    Hat(
        61,
        "Cat Head Hat",
        "hat0061.png"
    ),
    Hat(
        62,
        "Bat Wings",
        "hat0062.png"
    ),
    Hat(
        63,
        "Devil Horns",
        "hat0063.png"
    ),
    Hat(64, "Mohawk", "hat0064.png"),
    Hat(
        65,
        "Pumpkin Hat",
        "hat0065.png"
    ),
    Hat(
        66,
        "Spooky Paper Bag Hat",
        "hat0066.png"
    ),
    Hat(
        67,
        "Witch Hat",
        "hat0067.png"
    ),
    Hat(
        68,
        "Wolf Ears",
        "hat0068.png"
    ),
    Hat(
        69,
        "Pirate Hat",
        "hat0069.png"
    ),
    Hat(
        70,
        "Plague Doctor Mask",
        "hat0070.png"
    ),
    Hat(
        71,
        "Knife Hat",
        "hat0071.png"
    ),
    Hat(
        72,
        "Hockey Mask",
        "hat0072.png"
    ),
    Hat(
        73,
        "Miner Gear Hat",
        "hat0073.png"
    ),
    Hat(
        74,
        "Winter Gear Hat",
        "hat0074.png"
    ),
    Hat(
        75,
        "Archaeologist Hat",
        "hat0075.png"
    ),
    Hat(76, "Antenna", "hat0076.png"),
    Hat(77, "Balloon", "hat0077.png"),
    Hat(
        78,
        "Bird Nest",
        "hat0078.png"
    ),
    Hat(
        79,
        "Black Bandanna",
        "hat0079.png"
    ),
    Hat(
        80,
        "Caution Sign Hat",
        "hat0080.png"
    ),
    Hat(81, "Chef Hat", "hat0081.png"),
    Hat(82, "CCC Cap", "hat0082.png"),
    Hat(83, "Do-rag", "hat0083.png"),
    Hat(
        84,
        "Dum Sticky Note",
        "hat0084.png"
    ),
    Hat(85, "Fez", "hat0085.png"),
    Hat(
        86,
        "General Hat",
        "hat0086.png"
    ),
    Hat(
        87,
        "Pompadour",
        "hat0087.png"
    ),
    Hat(
        88,
        "Hunter Hat",
        "hat0088.png"
    ),
    Hat(
        89,
        "Military Helmet",
        "hat0089.png"
    ),
    Hat(
        90,
        "Mini Crewmate",
        "hat0090.png"
    ),
    Hat(
        91,
        "Ninja Mask",
        "hat0091.png"
    ),
    Hat(
        92,
        "Ram Horns",
        "hat0092.png"
    ),
    Hat(
        93,
        "Snow Crewmate",
        "hat0093.png"
    )

)

val skins = listOf<Skin>(
    Skin(0, "None", "skin0000.png"),
    Skin(1, "Astronaut", "skin0001.png"),
    Skin(2, "Captain", "skin0002.png"),
    Skin(3, "Mechanic", "skin0003.png"),
    Skin(4, "Military", "skin0004.png"),
    Skin(5, "Police", "skin0005.png"),
    Skin(6, "Doctor", "skin0006.png"),
    Skin(7, "Black Suit", "skin0007.png"),
    Skin(8, "White Suit", "skin0008.png"),
    Skin(9, "Wall Guard Suit", "skin0009.png"),
    Skin(10, "MIRA Hazmat", "skin0010.png"),
    Skin(11, "MIRA Security Guard", "skin0011.png"),
    Skin(12, "MIRA Landing", "skin0012.png"),
    Skin(13, "Miner Gear", "skin0013.png"),
    Skin(14, "Winter Gear", "skin0014.png"),
    Skin(15, "Archaeologist", "skin0015.png")
)

val pets = listOf<Pet>(
    Pet(0, "None", "pet0000.png"),
    Pet(1, "Brainslug", "pet0001.png"),
    Pet(2, "Mini Crewmate", "pet0002.png"),
    Pet(3, "Dog", "pet0003.png"),
    Pet(4, "Henry", "pet0004.png"),
    Pet(5, "Hamster", "pet0005.png"),
    Pet(6, "Robot", "pet0006.png"),
    Pet(7, "UFO", "pet0007.png"),
    Pet(8, "Ellie", "pet0008.png"),
    Pet(9, "Squig", "pet0009.png"),
    Pet(10, "Bedcrab", "pet0010.png")
)