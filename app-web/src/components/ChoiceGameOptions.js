import React, { Component } from 'react';
import service from '../service/GshowService';
import GameTimer from "./controls/FlatGameTimer";
import {ListGroup} from 'react-bootstrap';

import './ChoiceGameOptions.css';

export default class extends Component {

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

  render() {
    return (
      <div>
        <GameTimer className="timer" {...this.props.gameStatus} />
        <ListGroup>
          {
            this.props.options.map(option =>
              <ListGroup.Item>
                <button className={this.getClassName(option)}
                        onClick={() => this.handleChoice(option)}
                        disabled={this.optionDisabled(option)}>{option.option}</button>
               </ListGroup.Item>
            )
          }
        </ListGroup>
      </div>
    );
  }

}
