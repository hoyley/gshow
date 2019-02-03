import React, { Component } from 'react'
import facade from '../api/Facade'

export default class extends Component {

  handleChoice(option) {
    this.setState({ chosen: option });
    facade.choiceGameAnswer(option);
  }

  playerMadeChoice() {
    return this.props.playerAnswers &&
      this.props.playerAnswers.some(ans => ans.id === this.props.myPlayer.id);
  }

  playerSelectedOption(option) {
    return this.playerMadeChoice() && this.state && this.state.chosen;
  }

  render() {
    const options = this.props.options.map(option =>
      !this.playerMadeChoice()
        ? <button key={option} onClick={() => this.handleChoice(option)}>{option}</button>
        : this.playerSelectedOption(option)
          ? <div key={option}><b>{option}</b></div>
          : <div key={option}>{option}</div>
    );
    
    return (
      <ul>
        {options}
      </ul>
    );
  }

}
