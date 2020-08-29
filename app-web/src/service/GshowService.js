import AppState from './AppState'
import uuid from 'uuid'

class GshowService {

  constructor(rootPath) {
    console.log("Starting the GameShow Service");

    this.rootPath = rootPath || "";
    this.appState = new AppState();
    this.instanceKey = uuid.v4();
  }

  startPolling() {
    setInterval(() => this.refresh(), 200);
  }

  startListening() {
    const eventSource = new EventSource(this.rootPath + "/register?instanceKey="+this.instanceKey);

    eventSource.onopen = e => console.log(`EventStream is open for instance [${this.instanceKey}]`);

    eventSource.onerror = e => {

      if (e.currentTarget.readyState === EventSource.CLOSED) {
        console.log(`Error on connection. EventStream is closed for instance [${this.instanceKey}]`);
      } else if (e.currentTarget.readyState === EventSource.CONNECTING) {
        console.log(`Lost connection. EventStream is reconnecting for instance [${this.instanceKey}]`);
      }
    };
    
    eventSource.addEventListener("state", message => {
      let jsonMessage = JSON.parse(message.data);
      this.appState.setState(jsonMessage);
    });
  }


  refresh() {
    return this.state().then(response => {
      this.appState.setState(response);
    }).catch(err => {
      console.error(err);
      this.appState.clear();
    });
  }

  state() {
    return fetch(this.rootPath + '/state').then(r => r.json());
  }

  registerPlayer(nickname) {
    return fetch(this.rootPath + '/registerPlayer', {
      method: 'post',
      headers: { 'content-type': 'text/plain' },
      body: nickname
    });
  }

  startGame() {
    return fetch(this.rootPath + '/game/choice/next');
  }

  myPlayer() {
    return fetch(this.rootPath + '/myPlayer')
      .then(d => d.json())
      .catch((e) => {})
  }

  choiceGameAnswer(choice) {
    return fetch(this.rootPath + '/game/choice', {
      method: 'post',
      headers: { 'content-type': 'text/plain' },
      body: choice
    });
  }

  chooseGame(index) {
    return fetch(this.rootPath + `/game/choice/goTo?num=${index}`);
  }

  endGame() {
    return fetch(this.rootPath + '/game/choice/end');
  }

  setGuessTime(time) {
    return fetch(this.rootPath + `/game/choice/guessTime?guessTimeSecs=${time}`, {
      method: 'post'
    });
  }
};

const service = new GshowService();

// Uncomment this for polling-based implementation
service.startPolling();

// Uncomment this for SSE-based implementation
// service.startListening();

export default service;
