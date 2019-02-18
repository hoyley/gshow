import React from 'react';
import './CircleContainer.css'

export default class extends React.Component {

  getRotateStyle(i) {

    let type = .6, //circle type - 1 whole, 0.5 half, 0.25 quarter
      radius = '12em', //distance from center
      start = -90, //shift start from 0
      numberOfElements = this.props.children.length,
      slice = 360 * type / numberOfElements;

    var rotate = slice * i + start;
    var rotateReverse = rotate * -1;

    return {
      transform: `translate(${radius}, ${radius}) rotate(${rotate}deg) translate(${radius}) rotate(${rotateReverse}deg)`
    };
  };

  render() {
    return (
      <ul className="circleContainer">
        {
          this.props.children.map((child, index) =>
            <li key={index} className="circleItem" style={this.getRotateStyle(index)}>
              {child}
            </li>
          )
        }
      </ul>
    )};
}
