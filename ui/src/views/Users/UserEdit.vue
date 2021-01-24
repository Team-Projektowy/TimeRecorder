<template>
  <div class="login-body d-flex justify-content-center align-items-center" style="min-width: 400px">
    <div class="bg-light shadow px-5 py-5 text-primary d-flex justify-content-center align-items-center flex-column">
      <div class="form-group">
        <label for="email">Email</label>
        <input v-model.trim="$v.credential.email.$model" :class="{ 'border-danger' : $v.credential.email.$error }" id="email" class="form-control" type="email">
        <div v-if="$v.credential.email.$error">
          <div class="text-danger" v-if="!$v.credential.email.required">Email jest wymagany.</div>
          <div class="text-danger" v-if="!$v.credential.email.email">Wpisz poprawny email.</div>
          <div class="text-danger" v-if="!$v.credential.email.maxLength">Email może zawierać conajwyżej 255 znaków.</div>
        </div>
      </div>
      <div class="form-group">
        <label for="firstName">Imię</label>
        <input v-model.trim="$v.credential.firstName.$model" :class="{ 'border-danger': $v.credential.firstName.$error }" class="form-control" id="firstName" type="text">
        <div v-if="$v.credential.firstName.$error">
          <div class="text-danger" v-if="!$v.credential.firstName.required">Imię jest wymagane.</div>
          <div class="text-danger" v-if="!$v.credential.firstName.maxLength">Imię może zawierać conajwyżej 255 znaków.</div>
        </div>
      </div>
      <div class="form-group">
        <label for="lastName">Nazwisko</label>
        <input v-model.trim="$v.credential.lastName.$model" :class="{ 'border-danger': $v.credential.lastName.$error }" id="lastName" class="form-control" type="text">
        <div v-if="$v.credential.lastName.$error">
          <div class="text-danger" v-if="!$v.credential.lastName.maxLength">Nazwisko może zawierać conajwyżej 255 znaków.</div>
        </div>
      </div>
      <div class="form-group">
        <label for="position">Stanowisko</label>
        <input v-model.trim="$v.credential.position.$model" :class="{ 'border-danger': $v.credential.position.$error }" id="position" class="form-control" type="text">
        <div v-if="$v.credential.position.$error">
          <div class="text-danger" v-if="!$v.credential.position.required">Stanowisko jest wymagane.</div>
          <div class="text-danger" v-if="!$v.credential.position.maxLength">Stanowisko może zawierać conajwyżej 255 znaków.</div>
        </div>
      </div>
      <div class="form-group">
        <label for="hoursAWeek">Liczba godzin tygodniowo</label>
        <input v-model.trim="$v.credential.hoursAWeek.$model" :class="{ 'border-danger': $v.credential.hoursAWeek.$error }" id="hoursAWeek" class="form-control" type="number" min="0" max="99">
        <div v-if="$v.credential.hoursAWeek.$error">
          <div class="text-danger" v-if="!$v.credential.hoursAWeek.required">Liczba godzin tygodniowo jest wymagana.</div>
          <div class="text-danger" v-if="!$v.credential.hoursAWeek.integer">Liczba godzin tygodniowo musi być liczbą</div>
          <div class="text-danger" v-if="!$v.credential.hoursAWeek.minValue">Liczba godzin tygodniowo nie może być mniejsza niż 0</div>
          <div class="text-danger" v-if="!$v.credential.hoursAWeek.maxValue">Liczba godzin tygodniowo nie może być większa niż 99</div>
        </div>
      </div>
      <div class="form-group text-left w-100">
        <b-form-checkbox
            v-model="credential.admin"
            value="true"
            unchecked-value="false"
        >
          Administrator
        </b-form-checkbox>
      </div>

      <div class="mt-3 text-center">
        <b-button @click="editUser" variant="outline-primary" block squared>Edytuj użytkownika</b-button>
        <p class="mt-3 text-danger" v-if="submitStatus === 'ERROR'">Proszę wypełnić poprawnie.</p>
      </div>
    </div>
  </div>
</template>

<script>
import { required, maxLength, email, integer, maxValue, minValue } from 'vuelidate/lib/validators'
export default {
  name: 'UserEdit',
  data() {
    return {
      credential: {
        email: "",
        firstName: "",
        lastName: "",
        position: "",
        hoursAWeek: null,
        admin: "false",
      },
      submitStatus: null,
      userId: null,
    }
  },
  created() {
    this.userId = this.$route.params.userId;
    this.$http.get(this.$serverUrl + "/users/" + this.userId)
        .then(res => {
          const user = res.body;
          this.credential.email = user.email;
          this.credential.firstName = user.firstName;
          this.credential.lastName = user.lastName;
          this.credential.hoursAWeek = user.hoursAWeek;
          this.credential.position = user.position;
          this.credential.admin = user.admin ? "true" : "false";
        })
  },
  validations: {
    credential: {
      email: {
        required,
        email,
        maxLength: maxLength(255)
      },
      firstName: {
        required,
        maxLength: maxLength(255),
      },
      lastName: {
        maxLength: maxLength(255),
      },
      position: {
        required,
        maxLength: maxLength(255),
      },
      hoursAWeek: {
        required,
        integer,
        maxValue: maxValue(99),
        minValue: minValue(0),
      },
      admin: {
        required,
        boolean: val => val === "true" || val === "false",
      }
    }
  },
  methods: {
    editUser() {
      if (this.$v.$invalid) {
        this.submitStatus = 'ERROR'
      }
      else {
        this.$http.put(this.$serverUrl + '/users/' + this.userId, this.credential)
            .then(() => {
                this.$router.push({ name: "UsersAll" });
            })
      }
    },
  }
}
</script>

<style scoped>
input {
  min-width: 350px;
}
</style>
