package org.stepic.droid.test_utils.generators

import org.stepic.droid.model.User

object FakeUserGenerator {

    @JvmOverloads
    fun generate(id: Long = 0,
                 firstName: String = "John",
                 lastName: String = "Doe",
                 avatar: String? = null,
                 shortBio: String = "",
                 details: String = ""
    ): User {
        return User(id = id.toInt(),
                avatar = avatar,
                first_name = firstName,
                last_name = lastName,
                short_bio = shortBio,
                details = details)
    }
}
