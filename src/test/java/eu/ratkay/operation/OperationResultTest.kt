package eu.ratkay.operation

import com.github.karsaig.approvalcrest.jupiter.MatcherAssert.assertThat
import com.github.karsaig.approvalcrest.jupiter.matcher.Matchers.sameJsonAsApproved
import org.hl7.fhir.r4.model.Appointment
import org.hl7.fhir.r4.model.HumanName
import org.hl7.fhir.r4.model.OperationOutcome
import org.hl7.fhir.r4.model.OperationOutcome.IssueSeverity
import org.hl7.fhir.r4.model.Parameters
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.Reference
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.r4.model.StringType
import org.junit.jupiter.api.Test

class OperationResultTest {

    /*
        Hash: 5c96b9
     */
    @Test
    fun `get resource when initial resource is not OperationOutcome`() {
        // GIVEN
        val patient = getPatient()

        // WHEN
        val operationResult = OperationResult.of(patient)

        // THEN
        assertThat(operationResult.resource, sameJsonAsApproved())
    }

    /*
        Hash: a4f839
     */
    @Test
    fun `get resource when initial resource is not OperationOutcome with named Resource`() {
        // GIVEN
        val patient = getPatient()

        // WHEN
        val operationResult = OperationResult.of(patient, "patient")

        // THEN
        assertThat(operationResult.resource, sameJsonAsApproved())
    }

    /*
        Hash: 3876de
     */
    @Test
    fun `get resource when initial resource is OperationOutcome but not FATAL or ERROR`() {
        // GIVEN
        val operationOutcome = OperationOutcome()

        // WHEN
        val operationResult = OperationResult.of(operationOutcome)

        // THEN
        assertThat(operationResult.resource, sameJsonAsApproved())
    }

    /*
        Hash: 36cb0a
     */
    @Test
    fun `get resource when initial resource is OperationOutcome with FATAL issue`() {
        // GIVEN
        val operationOutcome = getOperationOutcome(IssueSeverity.FATAL)

        // WHEN
        val operationResult = OperationResult.of(operationOutcome)

        // THEN
        assertThat(operationResult.resource, sameJsonAsApproved())
    }

    /*
        Hash: 2a55bf
     */
    @Test
    fun `get resource when initial resource is OperationOutcome with ERROR issue`() {
        // GIVEN
        val operationOutcome = getOperationOutcome(IssueSeverity.ERROR)

        // WHEN
        val operationResult = OperationResult.of(operationOutcome)

        // THEN
        assertThat(operationResult.resource, sameJsonAsApproved())
    }

    /*
        Hash: a7344d
     */
    @Test
    fun `operate when initial resource is not OperationOutcome`() {
        // GIVEN
        val patient = getPatient()

        // WHEN
        val operationResult = OperationResult.of(patient)
            .operate {
                getAppointment()
            }

        // THEN
        assertThat(operationResult.resource, sameJsonAsApproved())
    }

    /*
        Hash: a90e46
     */
    @Test
    fun `operate named when initial resource is not OperationOutcome`() {
        // GIVEN
        val patient = getPatient()

        // WHEN
        val operationResult = OperationResult.of(patient)
            .operate("My Custom Appointment") {
                getAppointment()
            }

        // THEN
        assertThat(operationResult.resource, sameJsonAsApproved())
    }

    /*
        Hash: 63d1fb
     */
    @Test
    fun `operate when initial resource is OperationOutcome but ERROR`() {
        // GIVEN
        val operationOutcome = getOperationOutcome(IssueSeverity.ERROR)

        // WHEN
        val operationResult = OperationResult.of(operationOutcome)
            .operate {
                getAppointment()
            }

        // THEN
        assertThat(operationResult.resource, sameJsonAsApproved())
    }

    /*
        Hash: fa2270
     */
    @Test
    fun `operate named when initial resource is OperationOutcome but ERROR`() {
        // GIVEN
        val operationOutcome = getOperationOutcome(IssueSeverity.ERROR)

        // WHEN
        val operationResult = OperationResult.of(operationOutcome)
            .operate("Error will show") {
                getAppointment()
            }

        // THEN
        assertThat(operationResult.resource, sameJsonAsApproved())
    }

    /*
        Hash: d5a1c0
     */
    @Test
    fun `operate combined when initial resource is not OperationOutcome`() {
        // GIVEN
        val patient = getPatient()

        // WHEN
        val operationResult = OperationResult.of(patient)
            .operateCombined {
                getAppointment()
            }

        // THEN
        assertThat(operationResult.resource, sameJsonAsApproved())
    }

    /*
        Hash: 830218
     */
    @Test
    fun `operate combined when initial resource is not OperationOutcome and add multiple`() {
        // GIVEN
        val patient = getPatient()

        // WHEN
        val operationResult = OperationResult.of(patient)
            .operateCombined { getAppointment() }
            .operateCombined { getOperationOutcome(IssueSeverity.INFORMATION) }
            .operateCombined { getParameters("theName", Patient()) }

        // THEN
        assertThat(operationResult.resource, sameJsonAsApproved())
    }

    /*
        Hash: fdb30a
     */
    @Test
    fun `operate combined when initial resource is not OperationOutcome and add multiple ERROR created during operation chain`() {
        // GIVEN
        val patient = getPatient()

        // WHEN
        val operationResult = OperationResult.of(patient)
            .operateCombined { getAppointment() }
            .operateCombined { getOperationOutcome(IssueSeverity.ERROR) }
            .operateCombined { getParameters("theName", Patient()) }

        // THEN
        assertThat(operationResult.resource, sameJsonAsApproved())
    }

    /*
        Hash: c762d8
     */
    @Test
    fun `operate resource combined when initial resource is not OperationOutcome`() {
        // GIVEN
        val patient = getPatient()

        // WHEN
        val operationResult = OperationResult.of(patient)
            .operateResourceCombined { patientResource -> getAppointment(patientResource) }

        // THEN
        assertThat(operationResult.resource, sameJsonAsApproved())
    }

    /*
        Hash: b768cc
     */
    @Test
    fun `operate resource combined when initial resource is OperationOutcome but ERROR`() {
        // GIVEN
        val patient = getPatient()

        // WHEN
        val operationResult = OperationResult.of(patient)
            .operateResourceCombined { _ -> getOperationOutcome(IssueSeverity.ERROR) }

        // THEN
        assertThat(operationResult.resource, sameJsonAsApproved())
    }

    /*
        Hash: 7192d9
     */
    @Test
    fun `operate resource combined when initial resource is OperationOutcome but INFORMATION`() {
        // GIVEN
        val patient = getPatient()

        // WHEN
        val operationResult = OperationResult.of(patient)
            .operateResourceCombined { _ -> getOperationOutcome(IssueSeverity.INFORMATION) }

        // THEN
        assertThat(operationResult.resource, sameJsonAsApproved())
    }

    /*
        Hash: ddbdc4
     */
    @Test
    fun `operate combined when initial resource is OperationOutcome but ERROR`() {
        // GIVEN
        val operationOutcome = getOperationOutcome(IssueSeverity.ERROR)

        // WHEN
        val operationResult = OperationResult.of(operationOutcome)
            .operateCombined {
                getAppointment()
            }

        // THEN
        assertThat(operationResult.resource, sameJsonAsApproved())
    }

    /*
        Hash: bc2232
     */
    @Test
    fun `operate on Resource when initial resource is not OperationOutcome`() {
        // GIVEN
        val patient = getPatient()

        // WHEN
        val operationResult = OperationResult.of(patient)
            .operateResource { resource ->
                getAppointment(resource)
            }

        // THEN
        assertThat(operationResult.resource, sameJsonAsApproved())
    }

    /*
        Hash: b697f6
     */
    @Test
    fun `operate on Resource when initial resource is OperationOutcome but ERROR`() {
        // GIVEN
        val operationOutcome = getOperationOutcome(IssueSeverity.ERROR)

        // WHEN
        val operationResult = OperationResult.of(operationOutcome)
            .operateResource { resource ->
                getAppointment(resource)
            }

        // THEN
        assertThat(operationResult.resource, sameJsonAsApproved())
    }

    /*
        Hash: dfb4e6
     */
    @Test
    fun `operate on Parameters when initial resource is not OperationOutcome`() {
        // GIVEN
        val patient = getPatient()

        // WHEN
        val operationResult = OperationResult.of(patient, "patient")
            .operateParameters { params ->
                getAppointment(params.getParameter("patient").resource)
            }

        // THEN
        assertThat(operationResult.resource, sameJsonAsApproved())
    }

    /*
        Hash: 7bf7f5
     */
    @Test
    fun `operate on Parameters combined when initial resource is not OperationOutcome`() {
        // GIVEN
        val patient = getPatient()

        // WHEN
        val operationResult = OperationResult.of(patient, "patient")
            .operateParametersCombined { params ->
                getAppointment(params.getParameter("patient").resource)
            }

        // THEN
        assertThat(operationResult.resource, sameJsonAsApproved())
    }

    /*
        Hash: ec3b62
     */
    @Test
    fun `operate on Parameters when initial resource is OperationOutcome but ERROR`() {
        // GIVEN
        val operationOutcome = getOperationOutcome(IssueSeverity.ERROR)

        // WHEN
        val operationResult = OperationResult.of(operationOutcome)
            .operateParameters { resource ->
                getAppointment(resource)
            }

        // THEN
        assertThat(operationResult.resource, sameJsonAsApproved())
    }

    /*
        Hash: 11e464
     */
    @Test
    fun `operate on Parameters combined when initial resource is OperationOutcome but ERROR`() {
        // GIVEN
        val operationOutcome = getOperationOutcome(IssueSeverity.ERROR)

        // WHEN
        val operationResult = OperationResult.of(operationOutcome)
            .operateParametersCombined { resource ->
                getAppointment(resource)
            }

        // THEN
        assertThat(operationResult.resource, sameJsonAsApproved())
    }

    /*
        Hash: 9e64e7
     */
    @Test
    fun `operate on Parameters combined when initial resource is OperationOutcome but INFORMATION`() {
        // GIVEN
        val operationOutcome = getOperationOutcome(IssueSeverity.INFORMATION)

        // WHEN
        val operationResult = OperationResult.of(operationOutcome)
            .operateParametersCombined { resource ->
                getAppointment(resource)
            }

        // THEN
        assertThat(operationResult.resource, sameJsonAsApproved())
    }

    private fun getPatient(): Patient {
        return Patient().apply {
            name = listOf(HumanName().apply {
                family = "Doe"
                given = listOf(StringType("John"))
            })
        }
    }

    private fun getOperationOutcome(issueSeverity: IssueSeverity): OperationOutcome {
        return OperationOutcome().apply {
            issue = listOf(OperationOutcome.OperationOutcomeIssueComponent().apply {
                severity = issueSeverity
            })
        }
    }

    private fun getParameters(theName: String, theResource: Resource): Parameters {
        return Parameters().apply {
            parameter = listOf(Parameters.ParametersParameterComponent().apply {
                name = theName
                resource = theResource
            })
        }
    }

    private fun getAppointment(resource: Resource? = OperationOutcome()): Appointment {
        return Appointment().apply {
            status = Appointment.AppointmentStatus.BOOKED
            participant = listOf(Appointment.AppointmentParticipantComponent().apply {
                actor = Reference(resource).apply {
                    display = resource?.fhirType()
                }
            })
        }
    }
}