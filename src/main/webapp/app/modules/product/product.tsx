import './product.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { Row, Col, Alert } from 'reactstrap';

export interface IProductProps extends StateProps, DispatchProps {}

/*export class Product extends React.Component<IProductProps>{
    render(){
        return(
            <div>
                <p>Alp</p>
            </div>

        );
    }
}*/
export class Product extends React.Component<IProductProps> {
  render() {
    return (
      <Row>
        <p>Alpppp</p>
      </Row>
    );
  }
}

const mapStateToProps = storeState => ({});

const mapDispatchToProps = {};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Product);
