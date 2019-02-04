import React from "react";
import Countdown from './controls/Countdown.js';

export default class extends React.Component {

  constructor() {
    super();
    this.state = {
      remainingTime: 10
    }
  }
  componentDidMount() {
    this.setState({ time: this.props.remainingTime })
  }
  
  render() {
    const time = this.state && this.state.time
    return <div>
      <Countdown remaining={time} size="100"/>
    </div>
  }
}
