package com.example.livrodereceita.model

class ReceitaResponse {

    var candidates: List<Candidate>? = null

}

class ReceitaRequest{
    var contents: MutableList<Content>?= null
}

class Candidate {
    var content: Content? = null
    var finishReason: String? = null
    var index: Int? = null
}

class Content {
    var parts: MutableList<Part>? = null
    var role: String? = null
}

class Part {
    var text: String? = null
    var thoughtSignature: String? = null
}

class GenericResponse <T> {
    var success: Boolean = false
    var exception: Exception? = null
    var objeto: T? = null
}