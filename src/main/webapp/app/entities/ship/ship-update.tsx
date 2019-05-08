import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { ICompany } from 'app/shared/model/company.model';
import { getEntities as getCompanies } from 'app/entities/company/company.reducer';
import { getEntity, updateEntity, createEntity, reset } from './ship.reducer';
import { IShip } from 'app/shared/model/ship.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IShipUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IShipUpdateState {
  isNew: boolean;
  userId: string;
  companyId: string;
}

export class ShipUpdate extends React.Component<IShipUpdateProps, IShipUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      userId: '0',
      companyId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getUsers();
    this.props.getCompanies();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { shipEntity } = this.props;
      const entity = {
        ...shipEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/ship');
  };

  render() {
    const { shipEntity, users, companies, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="poseidonApiGatewayApp.ship.home.createOrEditLabel">
              <Translate contentKey="poseidonApiGatewayApp.ship.home.createOrEditLabel">Create or edit a Ship</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : shipEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="ship-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="shipIdLabel" for="shipId">
                    <Translate contentKey="poseidonApiGatewayApp.ship.shipId">Ship Id</Translate>
                  </Label>
                  <AvField
                    id="ship-shipId"
                    type="text"
                    name="shipId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="firstNameLabel" for="firstName">
                    <Translate contentKey="poseidonApiGatewayApp.ship.firstName">First Name</Translate>
                  </Label>
                  <AvField id="ship-firstName" type="text" name="firstName" />
                </AvGroup>
                <AvGroup>
                  <Label id="lastNameLabel" for="lastName">
                    <Translate contentKey="poseidonApiGatewayApp.ship.lastName">Last Name</Translate>
                  </Label>
                  <AvField id="ship-lastName" type="text" name="lastName" />
                </AvGroup>
                <AvGroup>
                  <Label id="genderLabel">
                    <Translate contentKey="poseidonApiGatewayApp.ship.gender">Gender</Translate>
                  </Label>
                  <AvInput
                    id="ship-gender"
                    type="select"
                    className="form-control"
                    name="gender"
                    value={(!isNew && shipEntity.gender) || 'MALE'}
                  >
                    <option value="MALE">
                      <Translate contentKey="poseidonApiGatewayApp.Gender.MALE" />
                    </option>
                    <option value="FEMALE">
                      <Translate contentKey="poseidonApiGatewayApp.Gender.FEMALE" />
                    </option>
                    <option value="OTHER">
                      <Translate contentKey="poseidonApiGatewayApp.Gender.OTHER" />
                    </option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="emailLabel" for="email">
                    <Translate contentKey="poseidonApiGatewayApp.ship.email">Email</Translate>
                  </Label>
                  <AvField
                    id="ship-email"
                    type="text"
                    name="email"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      pattern: {
                        value: '^[^@s]+@[^@s]+.[^@s]+$',
                        errorMessage: translate('entity.validation.pattern', { pattern: '^[^@s]+@[^@s]+.[^@s]+$' })
                      }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="phoneLabel" for="phone">
                    <Translate contentKey="poseidonApiGatewayApp.ship.phone">Phone</Translate>
                  </Label>
                  <AvField
                    id="ship-phone"
                    type="text"
                    name="phone"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="addressLine1Label" for="addressLine1">
                    <Translate contentKey="poseidonApiGatewayApp.ship.addressLine1">Address Line 1</Translate>
                  </Label>
                  <AvField id="ship-addressLine1" type="text" name="addressLine1" />
                </AvGroup>
                <AvGroup>
                  <Label id="addressLine2Label" for="addressLine2">
                    <Translate contentKey="poseidonApiGatewayApp.ship.addressLine2">Address Line 2</Translate>
                  </Label>
                  <AvField id="ship-addressLine2" type="text" name="addressLine2" />
                </AvGroup>
                <AvGroup>
                  <Label id="cityLabel" for="city">
                    <Translate contentKey="poseidonApiGatewayApp.ship.city">City</Translate>
                  </Label>
                  <AvField id="ship-city" type="text" name="city" />
                </AvGroup>
                <AvGroup>
                  <Label id="countryLabel" for="country">
                    <Translate contentKey="poseidonApiGatewayApp.ship.country">Country</Translate>
                  </Label>
                  <AvField id="ship-country" type="text" name="country" />
                </AvGroup>
                <AvGroup>
                  <Label for="user.login">
                    <Translate contentKey="poseidonApiGatewayApp.ship.user">User</Translate>
                  </Label>
                  <AvInput id="ship-user" type="select" className="form-control" name="userId">
                    {users
                      ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.login}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="company.email">
                    <Translate contentKey="poseidonApiGatewayApp.ship.company">Company</Translate>
                  </Label>
                  <AvInput id="ship-company" type="select" className="form-control" name="companyId">
                    {companies
                      ? companies.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.email}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/ship" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  users: storeState.userManagement.users,
  companies: storeState.company.entities,
  shipEntity: storeState.ship.entity,
  loading: storeState.ship.loading,
  updating: storeState.ship.updating,
  updateSuccess: storeState.ship.updateSuccess
});

const mapDispatchToProps = {
  getUsers,
  getCompanies,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ShipUpdate);
