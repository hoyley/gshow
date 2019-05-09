import React from 'react';
import PlayerUpdate from './PlayerUpdate'
import './PlayerUpdates.css';
import classNames from 'classnames';

export default class extends React.Component {

  getPlayerAnswers() {
    return [...(this.props.playerAnswers || [])]
      .sort((p1, p2) => p2.points - p1.points);
  }

  getPlayerName(id) {
    let player = this.props.players && this.props.players.find(p => p.id === id);
    return player && player.nickname;
  }

  render() {
    return <div className={classNames(["playerUpdates", this.props.className])}>
      {
        this.getPlayerAnswers().map((answer) =>
          <PlayerUpdate playerName={this.getPlayerName(answer.id)}
                        {...answer}
          />
        )
      }
    </div>
  }
}
