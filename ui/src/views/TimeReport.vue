<template>
  <div class="container">
    <div class="row mb-4">
      <div class="col-sm-4">
        <b-form-select v-model="selectedUserId" :options="usersOptions">
          <template #first>
            <b-form-select-option :value="null" disabled>-- Wybierz pracownika --</b-form-select-option>
          </template>
        </b-form-select>
      </div>
      <div class="col-sm-4">
          <b-form-datepicker v-model="selectedStartingDate" placeholder="-- Wybierz datę początkową --"></b-form-datepicker>
      </div>
      <div class="col-sm-4">
        <b-form-datepicker v-model="selectedEndingDate" placeholder="-- Wybierz datę końcową --"></b-form-datepicker>
      </div>
    </div>

    <div class="text-center">
      <b-button variant="primary" squared @click="fetchTimeRecords" class="mb-4">Szukaj</b-button>
      <div v-show="validationMessage" class="text-danger">{{ validationMessage }}</div>
    </div>

    <div class="mt-4 text-center">
      <h3 class="mb-3" v-if="currentlyFetchedUser && currentlyFetchedStartingDate && currentlyFetchedEndingDate">{{ `(${currentlyFetchedUser.id}) ${currentlyFetchedUser.firstName} ${currentlyFetchedUser.lastName}, ${currentlyFetchedUser.position}, ${selectedStartingDate} - ${selectedEndingDate}`}}</h3>
      <h4 v-show="noTimeRecordsFound" class="text-danger">Nie znaleziono żadnych wyników</h4>
      <b-table v-if="timeRecordsFormatted.length > 0" striped hover :fields="tableFields" :items="timeRecordsFormatted">
        <template #cell(task)="data">
          <span v-b-tooltip.hover :title="data.value.description">{{ data.value.name }}</span>
        </template>
        <template #custom-foot>
          <td></td>
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
  name: "TimeReport",
  data() {
    return {
      selectedUserId: null,
      users: [],
      selectedStartingDate: null,
      selectedEndingDate: null,
      validationMessage: null,
      timeRecords: [],
      noTimeRecordsFound: false,
      currentlyFetchedUser: null,
      currentlyFetchedStartingDate: null,
      currentlyFetchedEndingDate: null,
      tableFields: [
        {
          key: "date",
          label: "Data",
          sortable: true,
        },
        {
          key: "startingTime",
          label: "Czas rozpoczęcia",
          sortable: true,
        },
        {
          key: "endingTime",
          label: "Czas zakończenia",
          sortable: true,
        },
        {
          key: "length",
          label: "Długość",
          sortable: true,
        },
        {
          key: "task",
          label: "Czynność",
          sortable: true,
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
          date: timeRecord.startingTime.substring(0, 11),
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
      minutes = Math.ceil(sum / 60000);
      let hours = Math.floor(minutes / 60).toString();
      if (hours.length === 1) {
        hours = "0" + hours;
      }
      let minutes = (minutes % 60).toString();
      if (minutes.length === 1) {
        minutes = "0" + minutes;
      }
      return hours + ":" + minutes;
    }
  },
  methods: {
    fetchTimeRecords() {
      if (!this.selectedUserId && !this.selectedStartingDate && !this.selectedEndingDate) {
        this.validationMessage = "Proszę wybrać pracownika i datę";
      } else if (!this.selectedUserId) {
        this.validationMessage = "Proszę wybrać pracownika";
      } else if (!this.selectedStartingDate) {
        this.validationMessage = "Proszę wybrać datę";
      } else {
        this.$http.get(`${this.$serverUrl}/users/${this.selectedUserId}/time-records?startingDate=${this.selectedStartingDate}&endingDate=${this.selectedEndingDate}`)
            .then(res => {
              this.currentlyFetchedStartingDate = this.selectedStartingDate;
              this.currentlyFetchedEndingDate = this.selectedEndingDate;
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
