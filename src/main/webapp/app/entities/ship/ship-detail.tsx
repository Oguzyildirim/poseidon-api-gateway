import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './ship.reducer';
import { IShip } from 'app/shared/model/ship.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IShipDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ShipDetail extends React.Component<IShipDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { shipEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="poseidonApiGatewayApp.ship.detail.title">Ship</Translate> [<b>{shipEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="shipId">
                <Translate contentKey="poseidonApiGatewayApp.ship.shipId">Ship Id</Translate>
              </span>
            </dt>
            <dd>{shipEntity.shipId}</dd>
            <dt>
              <span id="firstName">
                <Translate contentKey="poseidonApiGatewayApp.ship.firstName">First Name</Translate>
              </span>
            </dt>
            <dd>{shipEntity.firstName}</dd>
            <dt>
              <span id="lastName">
                <Translate contentKey="poseidonApiGatewayApp.ship.lastName">Last Name</Translate>
              </span>
            </dt>
            <dd>{shipEntity.lastName}</dd>
            <dt>
              <span id="gender">
                <Translate contentKey="poseidonApiGatewayApp.ship.gender">Gender</Translate>
              </span>
            </dt>
            <dd>{shipEntity.gender}</dd>
            <dt>
              <span id="email">
                <Translate contentKey="poseidonApiGatewayApp.ship.email">Email</Translate>
              </span>
            </dt>
            <dd>{shipEntity.email}</dd>
            <dt>
              <span id="phone">
                <Translate contentKey="poseidonApiGatewayApp.ship.phone">Phone</Translate>
              </span>
            </dt>
            <dd>{shipEntity.phone}</dd>
            <dt>
              <span id="addressLine1">
                <Translate contentKey="poseidonApiGatewayApp.ship.addressLine1">Address Line 1</Translate>
              </span>
            </dt>
            <dd>{shipEntity.addressLine1}</dd>
            <dt>
              <span id="addressLine2">
                <Translate contentKey="poseidonApiGatewayApp.ship.addressLine2">Address Line 2</Translate>
              </span>
            </dt>
            <dd>{shipEntity.addressLine2}</dd>
            <dt>
              <span id="city">
                <Translate contentKey="poseidonApiGatewayApp.ship.city">City</Translate>
              </span>
            </dt>
            <dd>{shipEntity.city}</dd>
            <dt>
              <span id="country">
                <Translate contentKey="poseidonApiGatewayApp.ship.country">Country</Translate>
              </span>
            </dt>
            <dd>{shipEntity.country}</dd>
            <dt>
              <Translate contentKey="poseidonApiGatewayApp.ship.user">User</Translate>
            </dt>
            <dd>{shipEntity.userLogin ? shipEntity.userLogin : ''}</dd>
            <dt>
              <Translate contentKey="poseidonApiGatewayApp.ship.company">Company</Translate>
            </dt>
            <dd>{shipEntity.companyEmail ? shipEntity.companyEmail : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/ship" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/ship/${shipEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ ship }: IRootState) => ({
  shipEntity: ship.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ShipDetail);
