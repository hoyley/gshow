import facade from './api/Facade.js';
import Cookies from 'js-cookie'
export default class {

  constructor(onStateChange) {
    this.onStateChange = onStateChange;
    this.clear(true);
  }

  clear(suppress) {
    this.screen = "Welcome";
    this.players = [];
    this.playerId = null;
    this.myPlayer = null;

    if (!suppress) {
      this.onChange();
    }
  }

  start() {
    setInterval(() => this.refresh(), 200);
  }
  
  refresh() {
    facade.state().then(response => {
      this.setState(response.data);
    }).catch(err => {
      console.error(err);
      this.clear();
    });
  }

  setState(state) {
    if (!state || !state.screen || !state.screen.name) {
      this.clear();
      return;
    }

    this.screen = state.screen.name;

    if (state.registeredPlayers) {
      this.players = state.registeredPlayers;
    } else {
      this.players = [];
    }

    this.onChange();
  }

  onChange() {
    this.playerId = Cookies.get("player-id");
    this.myPlayer = this.players.find(p => p.id === this.playerId);
    this.onStateChange();
  }

}
