package com.example.fantasyland

import com.example.fantasyland.Card.*
import com.example.fantasyland.CardFace.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CardsTest: StringSpec ({
    "number of faces 1" {
        listOf(CARD_AS).numberOfFaces shouldBe 1
    }

    "number of faces 2" {
        listOf(CARD_AS, CARD_AD).shuffled().numberOfFaces shouldBe 1
    }

    "number of faces 3" {
        listOf(CARD_AS, CARD_2D).shuffled().numberOfFaces shouldBe 2
    }

    "number of faces 4" {
        listOf(CARD_3S, CARD_3D, CARD_3H).shuffled().numberOfFaces shouldBe 1
    }

    "number of faces 5" {
        listOf(CARD_3S, CARD_3D, CARD_4H).shuffled().numberOfFaces shouldBe 2
    }

    "number of faces 6" {
        listOf(CARD_3S, CARD_4D, CARD_5H).shuffled().numberOfFaces shouldBe 3
    }

    "number of faces 7" {
        listOf(CARD_4S, CARD_4D, CARD_4H, CARD_4C).shuffled().numberOfFaces shouldBe 1
    }

    "number of faces 8" {
        listOf(CARD_AS, CARD_2D, CARD_3H, CARD_4C, CARD_5H).shuffled().numberOfFaces shouldBe 5
    }

    "is of different faces 1" {
        listOf(CARD_AS).isOfDifferentFaces shouldBe true
    }

    "is of different faces 2" {
        listOf(CARD_AS, CARD_AD).shuffled().isOfDifferentFaces shouldBe false
    }

    "is of different faces 3" {
        listOf(CARD_AS, CARD_2D, CARD_3H, CARD_4C, CARD_5H).shuffled().isOfDifferentFaces shouldBe true
    }

    "is of one suit 1" {
        listOf(CARD_2S).isOfOneSuit shouldBe true
    }

    "is of one suit 2" {
        listOf(CARD_AD, CARD_2D, CARD_3D, CARD_4D, CARD_8D, CARD_KD).shuffled().isOfOneSuit shouldBe true
    }

    "is of one suit 3" {
        listOf(CARD_AS, CARD_2D).shuffled().isOfOneSuit shouldBe false
    }

    "faces histogram 1" {
        listOf(CARD_3S).facesHistogram shouldBe mapOf(THREE to 1)
    }

    "faces histogram 2" {
        listOf(CARD_5S, CARD_5D, CARD_5C, CARD_5H).shuffled().facesHistogram shouldBe mapOf(FIVE to 4)
    }

    "faces histogram 3" {
        listOf(CARD_5S, CARD_5D, CARD_5C, CARD_5H, CARD_AH).shuffled().facesHistogram shouldBe mapOf(FIVE to 4, ACE to 1)
    }

    "faces histogram 4" {
        listOf(CARD_5S, CARD_5D, CARD_6C, CARD_7H, CARD_AH).shuffled().facesHistogram shouldBe mapOf(FIVE to 2, ACE to 1, SIX to 1, SEVEN to 1)
    }

    "max count of faces 1" {
        listOf(CARD_KS).maxCountOfFaces shouldBe 1
    }

    "max count of faces 2" {
        listOf(CARD_5S, CARD_5D, CARD_5C, CARD_5H).shuffled().maxCountOfFaces shouldBe 4
    }

    "max count of faces 3" {
        listOf(CARD_5S, CARD_5D, CARD_5C, CARD_5H, CARD_AH, CARD_7C, CARD_7D).shuffled().maxCountOfFaces shouldBe 4
    }

    "is progression 1" {
        listOf(CARD_5S).isProgression shouldBe true
    }

    "is progression 2" {
        listOf(CARD_AS, CARD_2S, CARD_3D).shuffled().isProgression shouldBe true
    }

    "is progression 3" {
        listOf(CARD_KS, CARD_AS, CARD_2D).shuffled().isProgression shouldBe false
    }

    "is progression 4" {
        listOf(CARD_6S, CARD_7S, CARD_7D).shuffled().isProgression shouldBe false
    }

    "is progression 5" {
        listOf(CARD_9D, CARD_TH, CARD_JC, CARD_QC, CARD_KS, CARD_AS).shuffled().isProgression shouldBe true
    }

    "is wheel 1" {
        listOf(CARD_AS, CARD_2S, CARD_3D).shuffled().isWheel shouldBe false
    }

    "is wheel 2" {
        listOf(CARD_AS, CARD_2S, CARD_3D, CARD_4S, CARD_5H).shuffled().isWheel shouldBe true
    }

    "is wheel 3" {
        listOf(CARD_AS, CARD_KS, CARD_QD, CARD_JS, CARD_TH).shuffled().isWheel shouldBe false
    }

    "is wheel 4" {
        listOf(CARD_AS, CARD_2S, CARD_3S, CARD_4S, CARD_5S).shuffled().isWheel shouldBe false
    }

    "is steel wheel 1" {
        listOf(CARD_AS, CARD_2S, CARD_3S, CARD_4S, CARD_5S).shuffled().isSteelWheel shouldBe true
    }

    "is steel wheel 2" {
        listOf(CARD_AD, CARD_2S, CARD_3S, CARD_4S, CARD_5S).shuffled().isSteelWheel shouldBe false
    }

    "is any wheel 1" {
        listOf(CARD_AS, CARD_2S, CARD_3S, CARD_4S, CARD_5S).shuffled().isAnyWheel shouldBe true
    }

    "is any wheel 2" {
        listOf(CARD_AD, CARD_2S, CARD_3S, CARD_4S, CARD_5S).shuffled().isAnyWheel shouldBe true
    }
})