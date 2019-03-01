
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
    this.admin = false;

    if (!suppress) {
      this.onChange();
    }
  }

  setState(state) {
    if (!state || !state.globalState || !state.globalState.screen) {
      this.clear();
      return;
    }

    let globalState = state.globalState;

    this.screen = globalState.screen;
    this.gameConfig = globalState.choiceGameState;
    this.gameStatus = globalState.choiceGameState.status;
    this.myPlayer = state.myPlayer;
    this.admin = state.admin;

    if (globalState.registeredPlayers) {
      this.players = globalState.registeredPlayers;
    } else {
      this.players = [];
    }

    this.onChange();
  }

  onChange() {
    this.onStateChange && this.onStateChange();
  }
}
