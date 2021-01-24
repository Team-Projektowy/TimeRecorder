<template>
    <div class="container">
      <div class="d-flex justify-content-center">
        <div class="bg-light px-5 py-5 text-primary shadow d-flex justify-content-center align-items-center flex-column">
          <b-form-select v-model="timeRecord.taskId" :options="tasks" class="mb-3">
            <template #first>
              <b-form-select-option :value="null" disabled>
                Proszę wybrać zadanie jakie wykonujesz:
              </b-form-select-option>
            </template>
          </b-form-select>
          <b-form-textarea v-model="timeRecord.description" placeholder="Opis..." class="mb-3"></b-form-textarea>
          <b-button variant="success" v-on:click="clickStart" v-if="!timeRecord.startingTime">
            Start
          </b-button>
          <b-button variant="danger" v-on:click="clickStop" v-if="timeRecord.startingTime">
            Stop
          </b-button>
        </div>
      </div>
        <div class="mt-4">
          <b-table striped hover :items="userTimeRecords" :fields="fields">
            <template #cell(task)="data">
              <span v-b-tooltip.hover :title="data.value.description">{{ data.value.name }}</span>
            </template>
          </b-table>
        </div>
    </div>
</template>


<script>
export default {
    name: "Home",
    data() {
        return {
            fields: [{key: 'task', label: 'Zadania'}, {key: 'startingTime', label: 'Czas rozpoczęcia'}, {key: 'endingTime', label: 'Czas zakończenia'}, {key: 'description', label: 'Opis'}],
            tasks: [],
            userTimeRecords: [],
            timeRecord: {
                startingTime: null,
                endingTime: null,
                description: "",
                taskId: null,
            },
        };
    },
    methods: {
        fetchTasks() {
            this.$http.get(this.$serverUrl + "/tasks").then((res) => {
                res.data.forEach((element) => {
                    var temp = {};
                    temp.value = element.id;
                    temp.text = element.name;
                    this.tasks.push(temp);
                });
            });
        },
        fetchUserTimeRecords(){
            this.$http.get(`${this.$serverUrl}/users/me/time-records?startingDate=${new Date().toISOString().substring(0,10)}&endingDate=${new Date().toISOString().substring(0,10)}`)
            .then((response) => {
                this.userTimeRecords = response.data.map(record => {
                  return {
                    task: record.task,
                    startingTime: record.startingTime.substring(11,16),
                    endingTime: record.endingTime.substring(11,16),
                    description: record.description,
                  };
                });
            })
        },
        clickStart() {
            if (this.timeRecord.taskId === null) {
                alert("Wybierz jakie zadanie wykonujesz");
            } else {
                var currentTime = new Date().toISOString().replace("T", " ").substr(0, 19);
                this.timeRecord.startingTime = currentTime;
            }
        },
        clickStop() {
            var currentTime = new Date().toISOString().replace("T", " ").substr(0, 19);
            this.timeRecord.endingTime = currentTime;
            this.$http.post(this.$serverUrl + "/time-records", this.timeRecord)
                .then((response) => {
                    this.timeRecord.startingTime = null;
                    this.timeRecord.endingTime = null;
                    this.timeRecord.description = "";
                    this.timeRecord.startingTime = null;
                    alert("Udało się zapisać");
                    this.fetchUserTimeRecords();
            })
        },
    },
    mounted() {
        this.fetchTasks();
        this.fetchUserTimeRecords();
    },
};
</script>
