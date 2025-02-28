package com.example.flexcitytso.model

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.NoArgsConstructor
import java.time.DayOfWeek

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
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