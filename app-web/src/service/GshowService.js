import AppState from './AppState'

class GshowService {

  constructor(rootPath) {
    this.rootPath = rootPath || "";
    this.appState = new AppState();
    setInterval(() => this.refresh(), 200);
    this.refreshMyPlayer();
  }

  refresh() {
    this.state().then(response => {
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
    }).then(() => this.refreshMyPlayer());
  }

  startGame() {
    fetch(this.rootPath + '/startGame');
  }

  myPlayer() {
    return fetch(this.rootPath + '/myPlayer')
      .then(d => d.json())
      .catch((e) => {})
  }

  choiceGameAnswer(choice) {
    fetch(this.rootPath + '/game/choice', {
      method: 'post',
      headers: { 'content-type': 'text/plain' },
      body: choice
    });
  }

  refreshMyPlayer() {
    this.myPlayer()
      .then(player => {
        this.appState.myPlayer = player;
        this.appState.onChange();
      });
  }
};

const service = new GshowService();
export default service;
