package com.kinorlev.appengine

import com.kinorlev.appengine.v1.controllers.AnonymousController
import com.kinorlev.appengine.v1.model.CalculatePwfBody
import com.kinorlev.appengine.v1.model.CalculatePwfResponse
import com.kinorlev.appengine.v1.models.PwfData
import com.kinorlev.appengine.v1.usecases.controllersusecases.CalculatePwfUseCase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AppengineApplicationTests {

	@Autowired
	private lateinit var SUT: AnonymousController

	@Test
	fun contextLoads() {
		assertThat(SUT).isNotNull
	}

	private val fakeData = listOf<PwfData>(
		PwfData(101,30),
		PwfData(111,30),
		PwfData(121,40),
		PwfData(131,50),
		PwfData(141,30),
		PwfData(151,62),
		PwfData(161,71),
		PwfData(171,51),
		PwfData(181,41),
		PwfData(291,51),
		PwfData(201,30),
		PwfData(211,30),
		PwfData(221,40),
		PwfData(231,50),
		PwfData(241,30),
		PwfData(251,62),
		PwfData(261,71),
		PwfData(271,51),
		PwfData(281,41),
		PwfData(291,51),
		PwfData(301,30),
		PwfData(311,30),
		PwfData(321,40),
		PwfData(331,50),
		PwfData(341,30),
		PwfData(351,62),
		PwfData(361,71),
		PwfData(371,51),
		PwfData(381,41),
		PwfData(391,51),


		)

}
