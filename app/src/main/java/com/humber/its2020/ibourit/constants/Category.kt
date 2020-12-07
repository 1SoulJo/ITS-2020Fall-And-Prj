package com.humber.its2020.ibourit.constants

enum class Category(id: Int) {
    Shoes(0),
    Apparel(1),
    Watch(2),
    Car(3),
    RealEstate(4),
    Appliance(5),
    MobileDevice(6),
    Toy(7),
    TechGadget(8);

    companion object {
        fun byName(name: String): Category {
            return valueOf(name.replace("\\s".toRegex(), ""))
        }
    }
}