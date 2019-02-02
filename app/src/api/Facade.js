
import axios from 'axios';

export default {

  state() {
    return axios.get('/state');
  },

  registerPlayer(nickname) {
    axios({
      method: 'post',
      url: '/registerPlayer',
      headers: { 'content-type': 'text/plain' },
      data: nickname
    });
  }
}
