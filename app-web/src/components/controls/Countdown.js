import React from 'react'
import ReactCountdownClock from "react-countdown-clock";


export default (props) => {
  return <ReactCountdownClock seconds={props.remaining}
                              color="green"
                              alpha={1}
                              size={props.size} />
}
