import React from "react";
import "./AdminPanel.css";
import service from '../service/GshowService';
import {Button, Form} from "react-bootstrap";

export default class extends React.Component {

  constructor(props) {
    super(props);

    this.next = this.next.bind(this);
    this.previous = this.previous.bind(this);
    this.end = this.end.bind(this);
    this.handleGuessTime = this.handleGuessTime.bind(this);
    this.handleGuessTimeChange = this.handleGuessTimeChange.bind(this);

    this.state = {guessTime: 5};
  }

  previous() {
    if (this.props.currentGameIndex) {
      this.chooseGame(this.props.currentGameIndex - 1);
    } else {
      service.startGame().then(() => service.refresh()).then(() => {
        this.chooseGame(this.props.currentGameIndex - 2);
      });
    }
  }

  end() {
    service.endGame();
  }

  next() {
    service.startGame();
  }

  chooseGame(index) {
    service.chooseGame(index);
  }

  handleGuessTime(event) {
    event.preventDefault();
    service.setGuessTime(this.state.guessTime);

  }

  handleGuessTimeChange(event) {
    this.setState({guessTime: event.target.value});
  }

  render() {
    return <div className={this.props.className}>
      <div className="adminPanelContent">
        <label>Admin Mode ({this.props.currentGameIndex}): </label>
        <Button disabled={this.props.currentGameIndex <= 0} onClick={this.previous}>Previous</Button>
        <Button onClick={this.next}>Next</Button>
        <Button onClick={this.end}>End Current</Button>
        <Form className="guessTimeInput"
              onSubmit={this.handleGuessTime}>
          <Form.Control type="text"
                        placeholder="Guess Time"
                        maxLength="4"
                        value={this.state.guessTime}
                        onChange={this.handleGuessTimeChange} />
        </Form>
      </div>
    </div>
  }
  
}
