package com.example.appsqlitelesson

import android.provider.Settings.NameValueTable

data class DataSet(

        val studentId: Int,
        val nomeEstudante: String,
        val endereco : String,
        val classe : String,
        val idade: Int,
        val estudantePhoto: ByteArray?

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DataSet

        if (studentId != other.studentId) return false
        if (idade != other.idade) return false
        if (nomeEstudante != other.nomeEstudante) return false
        if (endereco != other.endereco) return false
        if (classe != other.classe) return false
        if (!estudantePhoto.contentEquals(other.estudantePhoto)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = studentId
        result = 31 * result + idade
        result = 31 * result + nomeEstudante.hashCode()
        result = 31 * result + endereco.hashCode()
        result = 31 * result + classe.hashCode()
        result = 31 * result + estudantePhoto.contentHashCode()
        return result
    }
}
