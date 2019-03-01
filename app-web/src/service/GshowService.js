import AppState from './AppState'

class GshowService {

  constructor(rootPath) {
    this.rootPath = rootPath || "";
    this.appState = new AppState();
    setInterval(() => this.refresh(), 200);
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
export default service;
