import React, { Component } from 'react';
import facade from '../api/Facade';
import CircleContainer from './controls/CircleContainer';
import './ChoiceGameOptions.css';
import GameTimer from "./GameTimer";

export default class extends Component {

  handleChoice(option) {
    if (!this.playerMadeChoice()) {
      this.setState({chosen: option});
      facade.choiceGameAnswer(option);
    }
  }

  playerMadeChoice() {
    return this.props.playerAnswers &&
      this.props.playerAnswers.some(ans => ans.id === this.props.myPlayer.id);
  }

  playerSelectedOption(option) {
    return this.playerMadeChoice() && this.state && this.state.chosen;
  }

  getClassName(option) {
    let className = "";

    if (option.eliminated) {
      className += " optionEliminated";
    } else if (!this.playerMadeChoice()) {
      className += " optionAvailable";
    } 

    if (this.playerSelectedOption(option) === option.option) {
      className += " optionMyChoice";
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
                 onClick={() => this.handleChoice(option.option)}>{option.option}</div>)
          }
        </CircleContainer>
      </div>
    );
  }

}
