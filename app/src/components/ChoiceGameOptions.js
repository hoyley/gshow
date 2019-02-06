import React, { Component } from 'react';
import facade from '../api/Facade';
import CircleContainer from './controls/CircleContainer';
import './ChoiceGameOptions.css';
import GameTimer from "./GameTimer";

export default class extends Component {

  handleChoice(option) {
    if (!this.playerMadeChoice()) {
      this.setState({chosen: option});
      facade.choiceGameAnswer(option.option);
    }
  }

  playerMadeChoice() {
    return this.props.playerAnswers &&
      this.props.playerAnswers.some(ans => ans.id === this.props.myPlayer.id);
  }

  playerSelectedOption(option) {
    return this.playerMadeChoice() && this.state && this.state.chosen && this.state.chosen.option === option.option;
  }

  optionDisabled(option) {
    return this.playerMadeChoice() || option.eliminated;
  }

  getClassName(option) {
    let className = "option";

    if (this.playerSelectedOption(option)) {
      className += " optionMyChoice";
    } else if (option.eliminated) {
      className += " optionEliminated";
    }
    
    return className;
  }

  render() {
    return (
      <div>
        <div className="timer">
          <GameTimer {...this.props.gameStatus} />
        </div>
        <CircleContainer>
          {
            this.props.options.map(option =>
            <div className={this.getClassName(option)}
                 onClick={() => this.handleChoice(option)}
                 disabled={this.optionDisabled(option)}>{option.option}
             </div>)
          }
        </CircleContainer>
      </div>
    );
  }

}
