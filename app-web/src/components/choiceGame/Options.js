import React, { Component } from 'react';
import service from '../../service/GshowService';
import GameTimer from "../controls/FlatGameTimer";

import './Options.css';

export default class extends Component {

  constructor(props) {
    super(props);

    this.maxCols = 2;
    this.preferredMaxRows = 5;
  }

  handleChoice(option) {
    if (!this.playerMadeChoice()) {
      this.setState({chosen: option});
      service.choiceGameAnswer(option.option);
    }
  }

  playerLoggedIn() {
    return !!this.props.myPlayer;
  }

  playerMadeChoice() {
    return this.props.playerAnswers &&
      this.props.playerAnswers.some(ans => this.playerLoggedIn() && ans.id === this.props.myPlayer.id);
  }

  playerSelectedOption(option) {
    return this.playerMadeChoice() && this.state && this.state.chosen && this.state.chosen.option === option.option;
  }

  optionDisabled(option) {
    return !this.playerLoggedIn() || this.playerMadeChoice() || option.eliminated;
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
  
  getNumColumns() {
    if (this.props.options.length > this.preferredMaxRows) {
      return Math.ceil(this.props.options.length / this.preferredMaxRows);
    } else {
      return 1;
    }
  }

  getColumnWidth() {
    return (100 / this.getNumColumns()) + "%";
  }

  render() {
    const buttonStyle = {
      'height': this.rowHeight + "px",
      'width': this.getColumnWidth()
    };

    return (
      <div>
        <GameTimer className="timer" {...this.props.gameStatus} />
        <div className="optionsContainer">
          {
            this.props.options.map(option =>
              <div style={buttonStyle}
                   className="optionContainer">
                <button className={this.getClassName(option)}
                        onClick={() => this.handleChoice(option)}
                        disabled={this.optionDisabled(option)}
                >{option.option}</button>
              </div>
            )
          }
        </div>
      </div>
    );
  }

}
