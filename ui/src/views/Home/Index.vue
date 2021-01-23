<template>
  <div class="d-flex justify-content-center align-items-center">
    <div class="bg-light px-5 py-5 text-primary shadow d-flex justify-content-center align-items-center flex-column">
        <b-form-select v-model="timeRecord.taskId" :options="tasks">
          <template #first>
            <b-form-select-option :value="null" disabled
              >Proszę wybrać zadanie jakie wykonujesz:</b-form-select-option
            >
          </template>
        </b-form-select>
        <b-form-textarea
          v-model="timeRecord.description"
          placeholder="Opis..."
        ></b-form-textarea>
        <b-button variant="success" v-on:click="clickStart" v-if="!timeRecord.startingTime"
          >Start</b-button
        >
        <b-button variant="danger" v-on:click="clickStop" v-if="timeRecord.startingTime"
          >Stop</b-button
        >
    </div>
  </div>
</template>


<script>
export default {
  name: "Home",
  data() {
    return {
      tasks: [],
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
      this.$http.get(this.$serverUrl + "/tasks").then((resoult) => {
        resoult.data.forEach((element) => {
          var q = {};
          q.value = element.id;
          q.text = element.name;
          this.tasks.push(q);
        });
      });
    },
    clickStart() {
      var currentTime = new Date()
        .toISOString()
        .replace("T", " ")
        .substr(0, 19);
      this.timeRecord.startingTime = currentTime;
    },
    clickStop() {
      var currentTime = new Date()
        .toISOString()
        .replace("T", " ")
        .substr(0, 19);
      this.timeRecord.endingTime = currentTime;
      console.log(this.timeRecord);
      this.$http
        .post(this.$serverUrl + "/time-records", this.timeRecord)
        .then((err) => {
          console.log(err);
        })
        .then(() => {
          location.reload();
        });
    },
  },
  mounted() {
    this.fetchTasks();
  },
};
</script>