package com.example.loadsheddingapp.data.remote

import com.example.loadsheddingapp.data.remote.dto.ScheduleDto
import com.example.loadsheddingapp.data.remote.dto.SuburbDto
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// MockLoadSheddingData provides a comprehensive offline dataset of South African suburbs across all 9 provinces
// (Gauteng, Western Cape, KwaZulu-Natal, Eastern Cape, Free State, Limpopo, Mpumalanga, North West, Northern Cape).
// It also features a dynamic 7-day schedule generator so any searched South African suburb has full schedule slots.
object MockLoadSheddingData {

    val mockSuburbs = listOf(
        // GAUTENG PROVINCE
        SuburbDto("jhb-sandton-01", "Sandton Central", "City of Johannesburg", "Gauteng", 2),
        SuburbDto("jhb-randburg-02", "Randburg", "City of Johannesburg", "Gauteng", 1),
        SuburbDto("jhb-rosebank-03", "Rosebank", "City of Johannesburg", "Gauteng", 2),
        SuburbDto("jhb-soweto-04", "Soweto (Orlando)", "City of Johannesburg", "Gauteng", 3),
        SuburbDto("jhb-midrand-05", "Midrand", "City of Johannesburg", "Gauteng", 2),
        SuburbDto("jhb-roodeport-06", "Roodepoort", "City of Johannesburg", "Gauteng", 1),
        SuburbDto("jhb-fourways-07", "Fourways", "City of Johannesburg", "Gauteng", 2),
        SuburbDto("jhb-braamfontein-08", "Braamfontein", "City of Johannesburg", "Gauteng", 1),
        SuburbDto("jhb-parkhurst-09", "Parkhurst", "City of Johannesburg", "Gauteng", 2),
        SuburbDto("jhb-alberton-10", "Alberton", "Ekurhuleni", "Gauteng", 1),
        SuburbDto("jhb-germiston-11", "Germiston", "Ekurhuleni", "Gauteng", 2),
        SuburbDto("jhb-benoni-12", "Benoni", "Ekurhuleni", "Gauteng", 3),
        SuburbDto("jhb-boksburg-13", "Boksburg", "Ekurhuleni", "Gauteng", 2),
        SuburbDto("jhb-kempton-14", "Kempton Park", "Ekurhuleni", "Gauteng", 1),
        SuburbDto("jhb-springs-15", "Springs", "Ekurhuleni", "Gauteng", 2),
        SuburbDto("pta-centurion-16", "Centurion", "City of Tshwane", "Gauteng", 2),
        SuburbDto("pta-central-17", "Pretoria Central", "City of Tshwane", "Gauteng", 1),
        SuburbDto("pta-hatfield-18", "Hatfield", "City of Tshwane", "Gauteng", 2),
        SuburbDto("pta-menlyn-19", "Menlyn", "City of Tshwane", "Gauteng", 1),
        SuburbDto("pta-brooklyn-20", "Brooklyn", "City of Tshwane", "Gauteng", 2),
        SuburbDto("pta-sunnyside-21", "Sunnyside", "City of Tshwane", "Gauteng", 1),
        SuburbDto("sed-vanderbijl-22", "Vanderbijlpark", "Sedibeng", "Gauteng", 2),
        SuburbDto("sed-vereeniging-23", "Vereeniging", "Sedibeng", "Gauteng", 1),
        SuburbDto("wrand-krugersdorp-24", "Krugersdorp", "West Rand", "Gauteng", 2),

        // WESTERN CAPE PROVINCE
        SuburbDto("cpt-seapoint-25", "Sea Point", "City of Cape Town", "Western Cape", 0),
        SuburbDto("cpt-campsbay-26", "Camps Bay", "City of Cape Town", "Western Cape", 0),
        SuburbDto("cpt-greenpoint-27", "Green Point", "City of Cape Town", "Western Cape", 0),
        SuburbDto("cpt-rondebosch-28", "Rondebosch", "City of Cape Town", "Western Cape", 1),
        SuburbDto("cpt-claremont-29", "Claremont", "City of Cape Town", "Western Cape", 1),
        SuburbDto("cpt-bellville-30", "Bellville", "City of Cape Town", "Western Cape", 1),
        SuburbDto("cpt-durbanville-31", "Durbanville", "City of Cape Town", "Western Cape", 0),
        SuburbDto("cpt-centurycity-32", "Century City", "City of Cape Town", "Western Cape", 1),
        SuburbDto("cpt-mitchellsplain-33", "Mitchells Plain", "City of Cape Town", "Western Cape", 2),
        SuburbDto("cpt-khayelitsha-34", "Khayelitsha", "City of Cape Town", "Western Cape", 2),
        SuburbDto("cpt-tableview-35", "Table View", "City of Cape Town", "Western Cape", 1),
        SuburbDto("wc-paarl-36", "Paarl", "Drakenstein", "Western Cape", 1),
        SuburbDto("wc-stellenbosch-37", "Stellenbosch", "Stellenbosch Municipality", "Western Cape", 1),
        SuburbDto("wc-somersetwest-38", "Somerset West", "City of Cape Town", "Western Cape", 1),
        SuburbDto("wc-george-39", "George", "George Municipality", "Western Cape", 2),
        SuburbDto("wc-knysna-40", "Knysna", "Knysna Municipality", "Western Cape", 1),
        SuburbDto("wc-mosselbay-41", "Mossel Bay", "Mossel Bay Municipality", "Western Cape", 1),
        SuburbDto("wc-hermanus-42", "Hermanus", "Overstrand", "Western Cape", 0),
        SuburbDto("wc-worcester-43", "Worcester", "Breede Valley", "Western Cape", 2),
        SuburbDto("wc-oudtshoorn-44", "Oudtshoorn", "Oudtshoorn Municipality", "Western Cape", 1),

        // KWAZULU-NATAL PROVINCE
        SuburbDto("dbn-umhlanga-45", "Umhlanga Rocks", "eThekwini", "KwaZulu-Natal", 3),
        SuburbDto("dbn-central-46", "Durban Central", "eThekwini", "KwaZulu-Natal", 2),
        SuburbDto("dbn-morningside-47", "Morningside", "eThekwini", "KwaZulu-Natal", 2),
        SuburbDto("dbn-berea-48", "Berea", "eThekwini", "KwaZulu-Natal", 1),
        SuburbDto("dbn-chatsworth-49", "Chatsworth", "eThekwini", "KwaZulu-Natal", 3),
        SuburbDto("dbn-phoenix-50", "Phoenix", "eThekwini", "KwaZulu-Natal", 3),
        SuburbDto("dbn-westville-51", "Westville", "eThekwini", "KwaZulu-Natal", 2),
        SuburbDto("dbn-hillcrest-52", "Hillcrest", "eThekwini", "KwaZulu-Natal", 1),
        SuburbDto("kzn-ballito-53", "Ballito", "KwaDukuza", "KwaZulu-Natal", 2),
        SuburbDto("kzn-pmb-54", "Pietermaritzburg Central", "Msunduzi", "KwaZulu-Natal", 2),
        SuburbDto("kzn-newcastle-55", "Newcastle", "Newcastle Municipality", "KwaZulu-Natal", 3),
        SuburbDto("kzn-richardsbay-56", "Richards Bay", "uMhlathuze", "KwaZulu-Natal", 2),
        SuburbDto("kzn-ladysmith-57", "Ladysmith", "Alfred Duma", "KwaZulu-Natal", 3),
        SuburbDto("kzn-margate-58", "Margate", "Ray Nkonyeni", "KwaZulu-Natal", 2),
        SuburbDto("kzn-portshepstone-59", "Port Shepstone", "Ray Nkonyeni", "KwaZulu-Natal", 2),

        // EASTERN CAPE PROVINCE
        SuburbDto("ec-gqeberha-60", "Gqeberha (Port Elizabeth)", "Nelson Mandela Bay", "Eastern Cape", 2),
        SuburbDto("ec-summerstrand-61", "Summerstrand", "Nelson Mandela Bay", "Eastern Cape", 1),
        SuburbDto("ec-walmer-62", "Walmer", "Nelson Mandela Bay", "Eastern Cape", 2),
        SuburbDto("ec-eastlondon-63", "East London Central", "Buffalo City", "Eastern Cape", 2),
        SuburbDto("ec-beaconbay-64", "Beacon Bay", "Buffalo City", "Eastern Cape", 1),
        SuburbDto("ec-gonubie-65", "Gonubie", "Buffalo City", "Eastern Cape", 1),
        SuburbDto("ec-kariega-66", "Kariega (Uitenhage)", "Nelson Mandela Bay", "Eastern Cape", 2),
        SuburbDto("ec-makhanda-67", "Makhanda (Grahamstown)", "Makana", "Eastern Cape", 3),
        SuburbDto("ec-mthatha-68", "Mthatha", "King Sabata Dalindyebo", "Eastern Cape", 3),
        SuburbDto("ec-jeffreysbay-69", "Jeffreys Bay", "Kouga", "Eastern Cape", 1),
        SuburbDto("ec-komani-70", "Komani (Queenstown)", "Enoch Mgijima", "Eastern Cape", 2),

        // FREE STATE PROVINCE
        SuburbDto("bloem-danpienaar-71", "Dan Pienaar", "Mangaung", "Free State", 1),
        SuburbDto("bloem-universitas-72", "Universitas", "Mangaung", "Free State", 1),
        SuburbDto("bloem-langenhoven-73", "Langenhovenpark", "Mangaung", "Free State", 2),
        SuburbDto("fs-welkom-74", "Welkom", "Matjhabeng", "Free State", 2),
        SuburbDto("fs-sasolburg-75", "Sasolburg", "Metsimaholo", "Free State", 2),
        SuburbDto("fs-kroonstad-76", "Kroonstad", "Moqhaka", "Free State", 2),
        SuburbDto("fs-harrismith-77", "Harrismith", "Maluti-a-Phofung", "Free State", 3),
        SuburbDto("fs-bethlehem-78", "Bethlehem", "Dihlabeng", "Free State", 2),

        // LIMPOPO PROVINCE
        SuburbDto("lim-polokwane-79", "Polokwane Central", "Polokwane Municipality", "Limpopo", 2),
        SuburbDto("lim-bendor-80", "Bendor", "Polokwane Municipality", "Limpopo", 1),
        SuburbDto("lim-florapark-81", "Flora Park", "Polokwane Municipality", "Limpopo", 2),
        SuburbDto("lim-thohoyandou-82", "Thohoyandou", "Thulamela", "Limpopo", 3),
        SuburbDto("lim-tzaneen-83", "Tzaneen", "Greater Tzaneen", "Limpopo", 2),
        SuburbDto("lim-mokopane-84", "Mokopane", "Mogalakwena", "Limpopo", 2),
        SuburbDto("lim-belabela-85", "Bela-Bela", "Bela-Bela Municipality", "Limpopo", 1),
        SuburbDto("lim-musina-86", "Musina", "Musina Municipality", "Limpopo", 2),

        // MPUMALANGA PROVINCE
        SuburbDto("mp-nelspruit-87", "Nelspruit (Mbombela)", "City of Mbombela", "Mpumalanga", 2),
        SuburbDto("mp-whiteriver-88", "White River", "City of Mbombela", "Mpumalanga", 1),
        SuburbDto("mp-barberton-89", "Barberton", "City of Mbombela", "Mpumalanga", 2),
        SuburbDto("mp-witbank-90", "Witbank (Emalahleni)", "eMalahleni", "Mpumalanga", 3),
        SuburbDto("mp-middelburg-91", "Middelburg", "Steve Tshwete", "Mpumalanga", 2),
        SuburbDto("mp-secunda-92", "Secunda", "Govan Mbeki", "Mpumalanga", 2),

        // NORTH WEST PROVINCE
        SuburbDto("nw-rustenburg-93", "Rustenburg Central", "Rustenburg Municipality", "North West", 2),
        SuburbDto("nw-potchefstroom-94", "Potchefstroom", "JB Marks", "North West", 1),
        SuburbDto("nw-klerksdorp-95", "Klerksdorp", "City of Matlosana", "North West", 2),
        SuburbDto("nw-mahikeng-96", "Mahikeng", "Mahikeng Municipality", "North West", 3),
        SuburbDto("nw-brits-97", "Brits", "Madibeng", "North West", 2),

        // NORTHERN CAPE PROVINCE
        SuburbDto("nc-kimberley-98", "Kimberley Central", "Sol Plaatje", "Northern Cape", 2),
        SuburbDto("nc-hadisonpark-99", "Hadison Park", "Sol Plaatje", "Northern Cape", 1),
        SuburbDto("nc-upington-100", "Upington", "Dawid Kruiper", "Northern Cape", 1),
        SuburbDto("nc-kuruman-101", "Kuruman", "Ga-Segonyana", "Northern Cape", 2),
        SuburbDto("nc-springbok-102", "Springbok", "Nama Khoi", "Northern Cape", 1)
    )

    // Dynamically generates 7-day schedule slots for any selected South African suburb ID.
    fun getMockSchedules(suburbId: String): List<ScheduleDto> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val calendar = Calendar.getInstance()
        val scheduleList = mutableListOf<ScheduleDto>()

        var idCounter = (suburbId.hashCode() and 0x7FFFFFFF) % 10000 + 100

        // Generate schedule slots for 7 consecutive days starting today
        for (dayIndex in 0..6) {
            val dateString = dateFormat.format(calendar.time)

            val dailySlots = listOf(
                ScheduleDto(
                    id = idCounter++,
                    suburbId = suburbId,
                    startTime = "06:00",
                    endTime = "08:30",
                    stage = (dayIndex % 3) + 1,
                    date = dateString
                ),
                ScheduleDto(
                    id = idCounter++,
                    suburbId = suburbId,
                    startTime = "14:00",
                    endTime = "16:30",
                    stage = ((dayIndex + 1) % 3) + 1,
                    date = dateString
                ),
                ScheduleDto(
                    id = idCounter++,
                    suburbId = suburbId,
                    startTime = "22:00",
                    endTime = "00:30",
                    stage = ((dayIndex + 2) % 3) + 1,
                    date = dateString
                )
            )

            scheduleList.addAll(dailySlots)
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        return scheduleList
    }
}
