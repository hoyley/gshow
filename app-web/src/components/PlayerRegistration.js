import React, { Component } from 'react';
import service from '../service/GshowService';
import {Form, Button} from 'react-bootstrap';
import './PlayerRegistration.css';

export default class extends Component {

  constructor(props) {
    super(props);

    this.state = {name: ''};

    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleChange(event) {
    this.setState({name: event.target.value});
  }

  handleSubmit(event) {
    event.preventDefault();
    service.registerPlayer(this.state.name);
  }

  render() {
    return (
      <Form onSubmit={this.handleSubmit}
            className="registrationForm">
        <Form.Label className="registrationLabel">Register:</Form.Label>
        <Form.Control className="registrationName"
                      type="text"
                      placeholder="Your Name"
                      maxLength="30"
                      value={this.state.name}
                      onChange={this.handleChange} />
        <Button className="registrationSubmit"
                variant="primary"
                type="submit">Submit</Button>
      </Form>

    );
  }
}
