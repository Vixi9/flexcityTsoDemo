package com.example.flexcitytso.model

import jakarta.persistence.*
import lombok.AllArgsConstructor
import java.time.DayOfWeek

@Entity
@AllArgsConstructor
class Asset(

    @Id var code: String,

    var name: String,

    // Activation cost per unit i.e per kW
    var activationCost: Double,

    //
    @ElementCollection
    @Enumerated(EnumType.STRING)
    var availabilities: MutableList<DayOfWeek>,

    // Volume of the asset in kW
    var volume: Int,

    )