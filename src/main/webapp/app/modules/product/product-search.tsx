import './product-search.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { Row, Col, Alert } from 'reactstrap';
import { MDBCol, MDBFormInline, MDBBtn } from 'mdbreact';

export interface IProductProps extends StateProps, DispatchProps {}

export class ProductSearch extends React.Component<IProductProps> {
  render() {
    return (
      <MDBCol md="12">
        <MDBFormInline className="md-form mr-auto mb-4">
          <input className="form-control mr-sm-2" type="text" placeholder="Search" aria-label="Search" />
          <MDBBtn outline color="warning" rounded size="sm" type="submit" className="mr-auto">
            Search
          </MDBBtn>
        </MDBFormInline>
      </MDBCol>
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
)(ProductSearch);
