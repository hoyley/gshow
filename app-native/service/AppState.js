
export default class {

  constructor(onStateChange) {
    this.onStateChange = onStateChange;
    this.clear(true);
  }

  setChangeHandler(onStateChange) {
    this.onStateChange = onStateChange;
  }

  clear(suppress) {
    this.screen = "Welcome";
    this.players = [];
    this.myPlayer = null;
    this.gameConfig = null;
    this.gameStatus = null;

    if (!suppress) {
      this.onChange();
    }
  }

  setState(state) {
    if (!state || !state.screen || !state.screen.name) {
      this.clear();
      return;
    }

    this.screen = state.screen.name;
    this.gameConfig = state.screen;
    this.gameStatus = state.screen.status;  

    if (state.registeredPlayers) {
      this.players = state.registeredPlayers;
    } else {
      this.players = [];
    }

    this.onChange();
  }

  onChange() {
    this.onStateChange && this.onStateChange();
  }
}
