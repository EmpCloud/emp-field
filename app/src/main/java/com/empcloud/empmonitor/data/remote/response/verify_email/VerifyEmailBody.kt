import java.io.Serializable

data class VerifyEmailBody(

    val status:String,
    val message:String
): Serializable