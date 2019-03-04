import React from "react";
import ProgressBar from 'react-bootstrap/ProgressBar'

export default class extends React.Component {

  constructor() {
    super();
    this.state = {};
  }
  componentDidMount() {
    this.setState({
      maxTime: this.props.remainingTime,
      maxPoints: this.props.remainingPoints
    });
  }

  render() {
    const time = this.props.remainingTime;
    const points = this.props.remainingPoints;
    
    return <ProgressBar className={this.props.className}>
      <ProgressBar variant="success" now={time / 2} max={this.state.maxTime} key={1} label={'Time: ' + time} />
      <ProgressBar variant="warning" now={points / 2} max={this.state.maxPoints} key={2} label={'Points: ' + points} />
    </ProgressBar>
  }
}
