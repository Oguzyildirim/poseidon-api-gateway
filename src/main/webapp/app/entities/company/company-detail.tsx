import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './company.reducer';
import { ICompany } from 'app/shared/model/company.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICompanyDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class CompanyDetail extends React.Component<ICompanyDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { companyEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="apigatewayApp.company.detail.title">Company</Translate> [<b>{companyEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="apigatewayApp.company.name">Name</Translate>
              </span>
            </dt>
            <dd>{companyEntity.name}</dd>
            <dt>
              <span id="email">
                <Translate contentKey="apigatewayApp.company.email">Email</Translate>
              </span>
            </dt>
            <dd>{companyEntity.email}</dd>
            <dt>
              <span id="phone">
                <Translate contentKey="apigatewayApp.company.phone">Phone</Translate>
              </span>
            </dt>
            <dd>{companyEntity.phone}</dd>
            <dt>
              <span id="addressLine1">
                <Translate contentKey="apigatewayApp.company.addressLine1">Address Line 1</Translate>
              </span>
            </dt>
            <dd>{companyEntity.addressLine1}</dd>
            <dt>
              <span id="addressLine2">
                <Translate contentKey="apigatewayApp.company.addressLine2">Address Line 2</Translate>
              </span>
            </dt>
            <dd>{companyEntity.addressLine2}</dd>
            <dt>
              <span id="city">
                <Translate contentKey="apigatewayApp.company.city">City</Translate>
              </span>
            </dt>
            <dd>{companyEntity.city}</dd>
            <dt>
              <span id="country">
                <Translate contentKey="apigatewayApp.company.country">Country</Translate>
              </span>
            </dt>
            <dd>{companyEntity.country}</dd>
            <dt>
              <Translate contentKey="apigatewayApp.company.user">User</Translate>
            </dt>
            <dd>{companyEntity.user ? companyEntity.user.login : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/company" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/company/${companyEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ company }: IRootState) => ({
  companyEntity: company.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CompanyDetail);
