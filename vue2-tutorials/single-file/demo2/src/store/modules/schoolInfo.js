const schoolInfo = {
  namespaced: true,
  state: {
    schoolName: "BunnySchool",
    schoolAddress: "昆山市印象欧洲",
  },
  getters: {
    getSchoolName(state) {
      return state.schoolName + "---";
    },
  },
  actions: {},
  mutations: {},
};

export default schoolInfo;
