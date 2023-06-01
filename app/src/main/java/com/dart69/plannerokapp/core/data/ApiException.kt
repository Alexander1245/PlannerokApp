package com.dart69.plannerokapp.core.data

import com.dart69.plannerokapp.core.domain.ErrorMessage
import java.io.IOException

class ApiException(val errorMessage: ErrorMessage) : IOException(errorMessage.message)