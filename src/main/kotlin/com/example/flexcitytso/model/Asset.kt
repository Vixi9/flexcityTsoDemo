package com.example.flexictytso.model

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

    var activationCost: Double,

    @ElementCollection
    @Enumerated(EnumType.STRING)
    var availabilities: MutableList<DayOfWeek>,

    var volume: Int,

    )