<template>
  <div class="container">
    <div class="row mb-4">
      <div class="col-sm-6">
        <b-form-select v-model="selectedUserId" :options="usersOptions">
          <template #first>
            <b-form-select-option :value="null" disabled>-- Wybierz pracownika --</b-form-select-option>
          </template>
        </b-form-select>
      </div>
      <div class="col-sm-6">
          <b-form-datepicker id="datepicker" v-model="selectedDate" placeholder="-- Wybierz datę --"></b-form-datepicker>
      </div>
    </div>
    <b-button variant="primary" @click="fetchTimeRecords" class="mb-4">Szukaj</b-button>
    <div v-show="validationMessage" class="text-danger">{{ validationMessage }}</div>
    <div class="mt-5">
      <h3 class="mb-3" v-if="currentlyFetchedUser && currentlyFetchedDate">{{ `(${currentlyFetchedUser.id}) ${currentlyFetchedUser.firstName} ${currentlyFetchedUser.lastName}, ${currentlyFetchedUser.position} - ${selectedDate}`}}</h3>
      <h4 v-show="noTimeRecordsFound" class="text-danger">Nie znaleziono żadnych wyników</h4>
      <b-table v-if="timeRecordsFormatted.length > 0" striped hover :fields="tableFields" :items="timeRecordsFormatted">
        <template #cell(task)="data">
          <span v-b-tooltip.hover :title="data.value.description">{{ data.value.name }}</span>
        </template>
        <template #custom-foot>
          <td></td>
          <td></td>
          <td class="font-weight-bold">{{ timeDifferenceSum }}</td>
          <td></td>
          <td></td>
        </template>
      </b-table>
    </div>
  </div>
</template>

<script>
export default {
  name: "Admin",
  data() {
    return {
      selectedUserId: null,
      users: [],
      selectedDate: null,
      validationMessage: null,
      timeRecords: [],
      noTimeRecordsFound: false,
      currentlyFetchedUser: null,
      currentlyFetchedDate: null,
      tableFields: [
        {
          key: "startingTime",
          label: "Czas rozpoczęcia",
        },
        {
          key: "endingTime",
          label: "Czas zakończenia",
        },
        {
          key: "length",
          label: "Długość",
        },
        {
          key: "task",
          label: "Czynność",
        },
        {
          key: "description",
          label: "Opis",
        },
      ]
    }
  },
  beforeMount() {
    this.$http.get(this.$serverUrl + "/users")
        .then(users => {
          this.users = users.body;
        })
  },
  computed: {
    usersOptions: function () {
      return this.users.map(user => {
        return {
          value: user.id,
          text: `(${user.id}) ${user.firstName} ${user.lastName}, ${user.position}`,
        }
      })
    },
    timeRecordsFormatted: function () {
      return this.timeRecords.map(timeRecord => {
        return {
          startingTime: timeRecord.startingTime.substring(11, 16),
          endingTime: timeRecord.endingTime.substring(11, 16),
          length: this.calculateTimeDifference(timeRecord.startingTime, timeRecord.endingTime),
          task: timeRecord.task,
          description: timeRecord.description,
        }
      })
    },
    timeDifferenceSum: function () {
      let sum = 0;
      this.timeRecords.forEach(timeRecord => {
        sum += (new Date(Date.parse(timeRecord.endingTime)) - new Date(Date.parse(timeRecord.startingTime)));
      })
      sum /= 1000;
      let hours = Math.floor(sum/3600).toString();
      if (hours.length === 1) {
        hours = "0" + hours;
      }
      let minutes = Math.ceil((sum - sum/3600)/60).toString();
      if (minutes.length === 1) {
        minutes = "0" + minutes;
      }
      return hours + ":" + minutes;
    }
  },
  methods: {
    fetchTimeRecords() {
      if (!this.selectedUserId && !this.selectedDate) {
        this.validationMessage = "Proszę wybrać pracownika i datę";
      } else if (!this.selectedUserId) {
        this.validationMessage = "Proszę wybrać pracownika";
      } else if (!this.selectedDate) {
        this.validationMessage = "Proszę wybrać datę";
      } else {
        this.$http.get(`${this.$serverUrl}/users/${this.selectedUserId}/time-records?date=${this.selectedDate}`)
            .then(res => {
              this.currentlyFetchedDate = this.selectedDate;
              this.currentlyFetchedUser = this.users.find(user => user.id === this.selectedUserId);
              this.timeRecords = res.body;
              this.noTimeRecordsFound = this.timeRecords.length === 0;
              this.validationMessage = null;
            });
      }
    },
    calculateTimeDifference(startingDateTime, endingDateTime) {
      startingDateTime = new Date(Date.parse(startingDateTime));
      endingDateTime = new Date(Date.parse(endingDateTime));
      const diff = (endingDateTime - startingDateTime) / 1000;
      let hours = Math.floor(diff/3600).toString();
      if (hours.length === 1) {
        hours = "0" + hours;
      }
      let minutes = Math.ceil((diff-diff/3600)/60).toString();
      if (minutes.length === 1) {
        minutes = "0" + minutes;
      }
      return hours + ":" + minutes;
    },
  }
}
</script>

<style scoped>

</style>
